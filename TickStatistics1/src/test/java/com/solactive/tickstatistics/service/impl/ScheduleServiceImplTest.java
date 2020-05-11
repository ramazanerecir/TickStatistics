package com.solactive.tickstatistics.service.impl;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.enums.CalculationType;
import com.solactive.tickstatistics.repository.TickRepository;
import com.solactive.tickstatistics.service.TickEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Spy
    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Mock
    private TickEventPublisher tickEventPublisher;

    @Mock
    private TickRepository tickRepository;

    @Captor
    protected ArgumentCaptor<String> tickCaptor;

    @Captor
    protected ArgumentCaptor<CalculationType> calculationTypeCaptor;

    @Test
    void recalculateStatistics() {
        String instrument = "IBM.N";

        when(tickRepository.getInstrumentList()).thenReturn(Arrays.asList(instrument, TickStatisticsConfiguration.aggregatedStatisticsName));
        scheduleService.recalculateStatistics();

        verify(scheduleService, times(2))
                .createTickEvent(tickCaptor.capture());

        List<String> capturedEvents = tickCaptor.getAllValues();

        assertEquals(capturedEvents.get(0), instrument);
        assertEquals(capturedEvents.get(1), TickStatisticsConfiguration.aggregatedStatisticsName);
    }

    @Test
    void createTickEvent() {
        String instrument = "IBM.N";

        scheduleService.createTickEvent(instrument);

        verify(tickEventPublisher, times(1))
                .publish(tickCaptor.capture(), calculationTypeCaptor.capture());

        assertEquals(tickCaptor.getValue(), instrument);
        assertEquals(calculationTypeCaptor.getValue(), CalculationType.SCHEDULED);
    }
}