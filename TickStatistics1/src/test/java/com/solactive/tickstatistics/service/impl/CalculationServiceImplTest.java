package com.solactive.tickstatistics.service.impl;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.InstrumentTick;
import com.solactive.tickstatistics.entity.Statistics;
import com.solactive.tickstatistics.entity.Tick;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CalculationServiceImplTest {

    //TODO - add calculation result validation tests

    @Spy
    @InjectMocks
    CalculationServiceImpl calculationService;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock
    TickStatisticsConfiguration tickStatisticsConfiguration;

    @Captor
    protected ArgumentCaptor<InstrumentTick> instrumentTickCaptor;

    @Captor
    protected ArgumentCaptor<Statistics> statisticsCaptor;

    @Captor
    protected ArgumentCaptor<Long> calculationTimeCaptor;

    @Captor
    protected ArgumentCaptor<String> stringCaptor;

    @Test
    void calculate() {
        InstrumentTick instrumentTick = new InstrumentTick();
        instrumentTick.setInstrument("IBM.N");

        calculationService.calculate(instrumentTick);
        verify(calculationService, times(1))
                .calculate(instrumentTickCaptor.capture(), calculationTimeCaptor.capture());

        assertEquals(instrumentTickCaptor.getValue().getInstrument(), instrumentTick.getInstrument());
    }

    @Test
    void calculateWithTimeStampNoTicks() {
        InstrumentTick instrumentTick = new InstrumentTick();
        instrumentTick.setInstrument("IBM.N");
        long calcTimestamp = System.currentTimeMillis();

        calculationService.calculate(instrumentTick, calcTimestamp);

        verify(calculationService, times(1))
                .sendToStatisticsQueue(statisticsCaptor.capture());

        assertEquals(statisticsCaptor.getValue().getInstrument(), instrumentTick.getInstrument());

    }

    @Test
    void calculateWithTimeStampWithTicks() {


    }

    @Test
    void calculateWithTimeStampHasStatistics() {
        InstrumentTick instrumentTick = new InstrumentTick();
        instrumentTick.setInstrument("IBM.N");
        instrumentTick.setTickList(Collections.singletonList(new Tick(10,System.currentTimeMillis()-100)));

        Statistics statistics = new Statistics();
        statistics.setCount(10);
        statistics.setInstrument(instrumentTick.getInstrument());
        instrumentTick.setStatistics(statistics);

        calculationService.calculate(instrumentTick, System.currentTimeMillis());

        verify(calculationService, times(1))
                .sendToStatisticsQueue(statisticsCaptor.capture());

        assertEquals(statisticsCaptor.getValue().getInstrument(), instrumentTick.getInstrument());
        assertEquals(statisticsCaptor.getValue().getCount(), 10);
    }

    @Test
    void sendToStatisticsQueue() {
        calculationService.sendToStatisticsQueue(new Statistics());

        verify(rabbitTemplate).convertAndSend(stringCaptor.capture(), stringCaptor.capture(),
                Mockito.any(Statistics.class));

    }
}