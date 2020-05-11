package com.solactive.tickstatistics.service.impl;

import com.solactive.tickstatistics.component.TickValidator;
import com.solactive.tickstatistics.entity.Statistics;
import com.solactive.tickstatistics.entity.dto.StatisticsDto;
import com.solactive.tickstatistics.repository.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private TickValidator tickValidator;

    @Test
    void getStatistics() {
        Statistics statistics = new Statistics();
        statistics.setAvg(100);
        statistics.setInstrumentUpdatedAt(System.currentTimeMillis());

        when(statisticsRepository.getAggregated()).thenReturn(statistics);
        when(tickValidator.validateTimestamp(statistics.getInstrumentUpdatedAt())).thenReturn(true);
        StatisticsDto statisticsDto = statisticsService.getStatistics();

        assertEquals(statistics.getAvg(), statisticsDto.getAvg());
    }

    @Test
    void getStatisticsNotValid() {
        Statistics statistics = new Statistics();
        statistics.setAvg(100);
        statistics.setInstrumentUpdatedAt(0L);

        when(statisticsRepository.getAggregated()).thenReturn(statistics);
        when(tickValidator.validateTimestamp(statistics.getInstrumentUpdatedAt())).thenReturn(false);
        StatisticsDto statisticsDto = statisticsService.getStatistics();

        assertEquals(0.0, statisticsDto.getAvg());
    }

    @Test
    void getStatisticsByInstrument() {
        String instrument = "IBM.N";
        Statistics statistics = new Statistics();
        statistics.setAvg(100);
        statistics.setInstrumentUpdatedAt(System.currentTimeMillis());

        when(statisticsRepository.get(instrument)).thenReturn(statistics);
        when(tickValidator.validateTimestamp(statistics.getInstrumentUpdatedAt())).thenReturn(true);
        StatisticsDto statisticsDto = statisticsService.getStatistics(instrument);

        assertEquals(statistics.getAvg(), statisticsDto.getAvg());
    }

    @Test
    void getStatisticsByInstrumentNotValid() {
        String instrument = "IBM.N";
        Statistics statistics = new Statistics();
        statistics.setAvg(100);
        statistics.setInstrumentUpdatedAt(0L);

        when(statisticsRepository.get(instrument)).thenReturn(statistics);
        when(tickValidator.validateTimestamp(statistics.getInstrumentUpdatedAt())).thenReturn(false);
        StatisticsDto statisticsDto = statisticsService.getStatistics(instrument);

        assertEquals(0.0, statisticsDto.getAvg());
    }
}