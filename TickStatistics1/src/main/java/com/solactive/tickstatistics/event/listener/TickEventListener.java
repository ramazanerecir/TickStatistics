package com.solactive.tickstatistics.event.listener;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.CalculationEvent;
import com.solactive.tickstatistics.entity.InstrumentTick;
import com.solactive.tickstatistics.entity.Statistics;
import com.solactive.tickstatistics.enums.CalculationType;
import com.solactive.tickstatistics.event.TickEventCreated;
import com.solactive.tickstatistics.repository.StatisticsRepository;
import com.solactive.tickstatistics.repository.TickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TickEventListener {

    private final TickRepository tickRepository;
    private final StatisticsRepository statisticsRepository;

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.calculation.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.calculation.routing.name}")
    private String routingName;

    @EventListener
    public void listenTickEvent(TickEventCreated tickEventCreated) {

        CalculationEvent calculationEvent = (CalculationEvent) tickEventCreated.getSource();

        sendToCalculationQueue(calculationEvent);
    }

    public void sendToCalculationQueue(CalculationEvent calculationEvent) {

        InstrumentTick instrumentTick;

        if(calculationEvent.getInstrument().equals(TickStatisticsConfiguration.aggregatedStatisticsName))
        {
            instrumentTick = tickRepository.getFilteredAllTicks();
        }
        else
        {
            instrumentTick = tickRepository.getFilteredInstrumentTick(calculationEvent.getInstrument());
        }

        Statistics statistics = statisticsRepository.get(calculationEvent.getInstrument());

        if(instrumentTick.getTickList().isEmpty() &&
              (statistics == null || statistics.getCount() == 0))
        {
            //No need to calculation
            return;
        }

        if(calculationEvent.getCalculationType() == CalculationType.SCHEDULED &&
                !instrumentTick.getTickList().isEmpty() &&
                statistics.getCount() == instrumentTick.getTickList().size() &&
                statistics.getInstrumentUpdatedAt() == instrumentTick.getUpdatedAt())
        {
            //No need to calculate all, Only Twap calculation is required
            instrumentTick.setStatistics(statistics.copy());
        }

        rabbitTemplate.convertAndSend(exchangeName, routingName, instrumentTick);
    }
}
