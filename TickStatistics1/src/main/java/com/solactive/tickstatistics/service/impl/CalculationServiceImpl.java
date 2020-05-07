package com.solactive.tickstatistics.service.impl;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.InstrumentTick;
import com.solactive.tickstatistics.entity.Statistics;
import com.solactive.tickstatistics.entity.Tick;
import com.solactive.tickstatistics.service.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    private final RabbitTemplate rabbitTemplate;
    private final TickStatisticsConfiguration tickStatisticsConfiguration;

    @Value("${rabbitmq.statistics.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.statistics.routing.name}")
    private String routingName;

    @Override
    @RabbitListener(queues = "${rabbitmq.calculation.queue.name}")
    public void calculate(InstrumentTick instrumentTick) {
        long calcTimestamp = new Timestamp(new Date().getTime()).getTime();
        calculate(instrumentTick, calcTimestamp);
    }

    @Override
    public void calculate(InstrumentTick instrumentTick, long calcTimestamp)
    {
        Statistics statistics;
        if (instrumentTick.getTickList() != null && !instrumentTick.getTickList().isEmpty()) {
            if(instrumentTick.getStatistics() != null) {
            /*
                If instrumentTick has statistics:
                Triggered from Scheduler, and tick list is not changed,
                so we only need to calculate twap and twap2
                which are changing according to calculation time
             */
                statistics = instrumentTick.getStatistics();
            }
            else {
                statistics = new Statistics();
                statistics.setInstrument(instrumentTick.getInstrument());
                statistics.setInstrumentUpdatedAt(instrumentTick.getUpdatedAt());

                //Sort by price to calculate volatility & quantile
                instrumentTick.getTickList().sort(Comparator.comparing(Tick::getPrice));

                //Count
                statistics.setCount(instrumentTick.getTickList().size());
                //Min
                statistics.setMin(scale(instrumentTick.getTickList().get(0).getPrice(), tickStatisticsConfiguration.getPriceScale()));
                //Max
                statistics.setMax(scale(instrumentTick.getTickList().get(instrumentTick.getTickList().size() - 1).getPrice(),
                        tickStatisticsConfiguration.getPriceScale()));
                //Avg
                statistics.setAvg(scale(instrumentTick.getTickList()
                                .parallelStream()
                                .mapToDouble(Tick::getPrice).sum() / statistics.getCount(),
                        tickStatisticsConfiguration.getPriceScale()));

                //Volatility
                statistics.setVolatility(scale(calculateVolatility(instrumentTick.getTickList(), statistics.getAvg()),
                        tickStatisticsConfiguration.getScale()));

                //Quantile
                statistics.setQuantile(scale(calculateQuantile(instrumentTick.getTickList()),
                        tickStatisticsConfiguration.getScale()));
            }

            double[] resultArray = calculateTwapAndDrawdown(instrumentTick.getTickList(), calcTimestamp);

            //Twap
            statistics.setTwap(scale(resultArray[0], tickStatisticsConfiguration.getScale()));
            //Twap2
            statistics.setTwap2(scale(resultArray[1], tickStatisticsConfiguration.getScale()));
            //MaxDrawdown is expected to not changed
            statistics.setMaxDrawdown(scale(resultArray[2], tickStatisticsConfiguration.getPriceScale()));
        }
        else
        {
            statistics = new Statistics();
            statistics.setInstrument(instrumentTick.getInstrument());
            statistics.setInstrumentUpdatedAt(instrumentTick.getUpdatedAt());
        }

        statistics.setCalculatedAt(calcTimestamp);
        sendToStatisticsQueue(statistics);
    }

    private double calculateVolatility(List<Tick> tickList, double avg)
    {
        double variance = tickList.parallelStream().mapToDouble(t -> Math.pow(t.getPrice() - avg, 2.0)).sum()
                / tickList.size();

        return Math.sqrt(variance);
    }

    private double calculateQuantile(List<Tick> tickList) {
        int i=0;
        double weight = 1.0/tickList.size();
        double totalWeight = 0.0;
        for(; i<tickList.size(); ++i)
        {
            totalWeight += weight;

            if(totalWeight == tickStatisticsConfiguration.getPercentile())
            {
                return tickList.get(i).getPrice();
            }
            else if(totalWeight > tickStatisticsConfiguration.getPercentile())
            {
                break;
            }
        }

        if(totalWeight < tickStatisticsConfiguration.getPercentile())
            return tickList.get(tickList.size()-1).getPrice();
        else if(i == 0)
            return tickList.get(0).getPrice();

        return tickList.get(i).getPrice() - (tickList.get(i).getPrice() - tickList.get(i-1).getPrice())
                * (totalWeight - tickStatisticsConfiguration.getPercentile()) / weight;
    }

    private double[] calculateTwapAndDrawdown(List<Tick> tickList, long calcTimestamp) {
        //Sort by timestamp to calculate twap and drawdown
        tickList.sort(Comparator.comparing(Tick::getTimestamp));

        double twap = 0.0;
        double twap2 = 0.0;
        double maxDrawdown = 0.0;

        for(int i=0; i<tickList.size()-1; ++i)
        {
            twap += tickList.get(i).getPrice()
                    * calculateTwapWeight(tickList.get(i).getTimestamp(),
                    tickList.get(i+1).getTimestamp());

            twap2 += tickList.get(i).getPrice()
                    * calculateTwap2Weight(tickList.get(i).getTimestamp(), calcTimestamp,
                    tickList.size()-i-1, tickList.size());

            if(i>0)
            {
                maxDrawdown = getDrawdownByPeak(tickList.get(i-1).getPrice(), tickList.get(i).getPrice(), maxDrawdown);
            }
        }

        twap += tickList.get(tickList.size()-1).getPrice()
                * calculateTwapWeight(tickList.get(tickList.size()-1).getTimestamp(), calcTimestamp);

        twap2 += tickList.get(tickList.size()-1).getPrice()
                * calculateTwap2Weight(tickList.get(tickList.size()-1).getTimestamp(), calcTimestamp,
                0, tickList.size());

        twap = twap / tickList.size();
        twap2 = twap2 / tickList.size();

        if(tickList.size() >= 2)
        {
            maxDrawdown = getDrawdownByPeak(tickList.get(tickList.size()-2).getPrice(),
                    tickList.get(tickList.size()-1).getPrice(), maxDrawdown);
        }

        return new double[] {twap, twap2, maxDrawdown};
    }

    private double getDrawdownByPeak(double prevTick, double nextTick, double maxDrawdown) {
        if(prevTick > nextTick)
        {
            double drawdown = prevTick - nextTick;
            if(drawdown > maxDrawdown)
                maxDrawdown = drawdown;
        }

        return maxDrawdown;
    }

    private double calculateTwapWeight(long timestamp, long nextTimestamp) {
        return ((double)(nextTimestamp - timestamp))/tickStatisticsConfiguration.getSlidingTimeInterval();
    }

    private double calculateTwap2Weight(long timestamp, long calcTimestamp, int index, int size) {
        double lambda = tickStatisticsConfiguration.getLambda() < 1.0 ?
                (1.0 - tickStatisticsConfiguration.getLambda()) / (Math.pow(tickStatisticsConfiguration.getLambda(), index))
                        / (Math.pow(tickStatisticsConfiguration.getLambda(), size))
                : 1.0;
        return ((double)(calcTimestamp - timestamp))/tickStatisticsConfiguration.getSlidingTimeInterval()
                * lambda;
    }

    private double scale(double price, int scale)
    {
        if(Double.isNaN(price) || Double.isInfinite(price)) {
            return price;
        }
        else {
            return BigDecimal.valueOf(price).setScale(scale, RoundingMode.DOWN).doubleValue();
        }
    }

    @Override
    public void sendToStatisticsQueue(Statistics statistics) {
        rabbitTemplate.convertAndSend(exchangeName, routingName, statistics);
    }
}
