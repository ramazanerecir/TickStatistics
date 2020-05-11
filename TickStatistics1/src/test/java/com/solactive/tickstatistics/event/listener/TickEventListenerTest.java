package com.solactive.tickstatistics.event.listener;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.CalculationEvent;
import com.solactive.tickstatistics.entity.InstrumentTick;
import com.solactive.tickstatistics.entity.Statistics;
import com.solactive.tickstatistics.entity.Tick;
import com.solactive.tickstatistics.enums.CalculationType;
import com.solactive.tickstatistics.event.TickEventCreated;
import com.solactive.tickstatistics.repository.StatisticsRepository;
import com.solactive.tickstatistics.repository.TickRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TickEventListenerTest {

    @Spy
    @InjectMocks
    TickEventListener tickEventListener;

    @Mock
    TickRepository tickRepository;

    @Mock
    StatisticsRepository statisticsRepository;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Captor
    ArgumentCaptor<CalculationEvent> calculationEventCaptor;

    @Captor
    ArgumentCaptor<InstrumentTick> instrumentTickCaptor;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    @Test
    void listenTickEvent() {
        String instrument = "IBM.N";
        CalculationEvent calculationEvent = new CalculationEvent(instrument, CalculationType.SCHEDULED);
        TickEventCreated tickEventCreated = new TickEventCreated(calculationEvent);

        InstrumentTick instrumentTick = new InstrumentTick();
        instrumentTick.setInstrument(instrument);
        instrumentTick.setTickList(Collections.singletonList(new Tick(100, System.currentTimeMillis())));

        when(tickRepository.getFilteredInstrumentTick(instrument)).thenReturn(instrumentTick);
        tickEventListener.listenTickEvent(tickEventCreated);

        verify(tickEventListener, times(1))
                .sendToCalculationQueue(calculationEventCaptor.capture());

        assertEquals(calculationEventCaptor.getValue(), calculationEvent);
    }

    @Test
    public void sendToCalculationQueue() {
        String instrument = "IBM.N";
        CalculationEvent calculationEvent = new CalculationEvent(instrument, CalculationType.NEWTICK);

        InstrumentTick instrumentTick = new InstrumentTick();
        instrumentTick.setInstrument(instrument);
        instrumentTick.setTickList(Collections.singletonList(new Tick(100, System.currentTimeMillis())));

        when(tickRepository.getFilteredInstrumentTick(instrument)).thenReturn(instrumentTick);
        tickEventListener.sendToCalculationQueue(calculationEvent);

        verify(rabbitTemplate, times(1))
                .convertAndSend(stringCaptor.capture(), stringCaptor.capture(), instrumentTickCaptor.capture());

        assertEquals(instrumentTickCaptor.getValue(), instrumentTick);
    }

    @Test
    public void sendToCalculationQueueWithStatistics() {
        String instrument = TickStatisticsConfiguration.aggregatedStatisticsName;
        CalculationEvent calculationEvent = new CalculationEvent(instrument, CalculationType.SCHEDULED);

        long timestamp = System.currentTimeMillis();
        InstrumentTick instrumentTick = new InstrumentTick();
        instrumentTick.setInstrument(instrument);
        instrumentTick.setUpdatedAt(timestamp);
        instrumentTick.setTickList(Collections.singletonList(new Tick(100, System.currentTimeMillis())));

        Statistics statistics = new Statistics();
        statistics.setCount(1);
        statistics.setInstrumentUpdatedAt(timestamp);

        when(tickRepository.getFilteredAllTicks()).thenReturn(instrumentTick);
        when(statisticsRepository.get(calculationEvent.getInstrument())).thenReturn(statistics);
        tickEventListener.sendToCalculationQueue(calculationEvent);

        verify(rabbitTemplate, times(1))
                .convertAndSend(stringCaptor.capture(), stringCaptor.capture(), instrumentTickCaptor.capture());

        assertEquals(instrumentTickCaptor.getValue().getStatistics(), statistics);
    }
}