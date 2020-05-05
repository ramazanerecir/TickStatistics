package com.solactive.tickstatistics.service.impl;

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

    @Test
    void getStatistics() {
        Statistics statistics = new Statistics();
        statistics.setAvg(100);

        when(statisticsRepository.getAggregated()).thenReturn(statistics);
        StatisticsDto statisticsDto = statisticsService.getStatistics();

        assertEquals(statistics.getAvg(), statisticsDto.getAvg());
    }

    @Test
    void getStatisticsByInstrument() {
        String instrument = "IBM.N";
        Statistics statistics = new Statistics();
        statistics.setAvg(100);

        when(statisticsRepository.get(instrument)).thenReturn(statistics);
        StatisticsDto statisticsDto = statisticsService.getStatistics(instrument);

        assertEquals(statistics.getAvg(), statisticsDto.getAvg());
    }
}