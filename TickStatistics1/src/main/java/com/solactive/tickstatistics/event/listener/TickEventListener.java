package com.solactive.tickstatistics.event.listener;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.InstrumentTick;
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

        String instrument = tickEventCreated.getSource().toString();

        if(instrument.equals(TickStatisticsConfiguration.aggregatedStatisticsName))
        {
            sendToCalculationQueue();
        }
        else
        {
            sendToCalculationQueue(instrument);
        }
    }

    private void sendToCalculationQueue(String instrument) {
        InstrumentTick instrumentTick = tickRepository.getFilteredInstrumentTick(instrument);

        if(!(instrumentTick.getTickList().isEmpty() &&
                (statisticsRepository.get(instrument) == null ||
                        statisticsRepository.get(instrument).getCount() == 0)))
        {
            rabbitTemplate.convertAndSend(exchangeName, routingName, instrumentTick);
        }
    }

    private void sendToCalculationQueue() {
        InstrumentTick instrumentTick = tickRepository.getFilteredAllTicks();

        if(!(instrumentTick.getTickList().isEmpty() &&
                (statisticsRepository.get(instrumentTick.getInstrument()) == null ||
                        statisticsRepository.get(instrumentTick.getInstrument()).getCount() == 0)))
        {
            rabbitTemplate.convertAndSend(exchangeName, routingName, instrumentTick);
        }
    }
}
