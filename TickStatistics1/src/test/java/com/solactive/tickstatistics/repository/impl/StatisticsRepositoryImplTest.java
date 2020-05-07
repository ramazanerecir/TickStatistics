package com.solactive.tickstatistics.repository.impl;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.Statistics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatisticsRepositoryImplTest {

    @InjectMocks
    StatisticsRepositoryImpl statisticsRepository;

    @Test
    void getAggregated() {
        Statistics statistics = new Statistics();
        statistics.setInstrument(TickStatisticsConfiguration.aggregatedStatisticsName);

        statisticsRepository.insert(statistics);
        Statistics result = statisticsRepository.getAggregated();

        assertEquals(result.getInstrument(), statistics.getInstrument());
    }

    @Test
    void get() {
        String instrument = "IBM.N";
        Statistics statistics = new Statistics();
        statistics.setInstrument(instrument);

        statisticsRepository.insert(statistics);
        Statistics result = statisticsRepository.get(instrument);

        assertEquals(result.getInstrument(), statistics.getInstrument());
    }


    @Test
    void insert() {
        String instrument = "IBM.N";
        Statistics statistics = new Statistics();
        statistics.setInstrument(instrument);

        statisticsRepository.insert(statistics);
        Statistics result = statisticsRepository.get(instrument);

        assertEquals(result.getInstrument(), statistics.getInstrument());
    }

}