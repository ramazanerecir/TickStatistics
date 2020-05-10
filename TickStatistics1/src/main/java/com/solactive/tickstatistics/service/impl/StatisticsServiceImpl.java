package com.solactive.tickstatistics.service.impl;

import com.solactive.tickstatistics.component.TickValidator;
import com.solactive.tickstatistics.entity.Statistics;
import com.solactive.tickstatistics.entity.dto.StatisticsDto;
import com.solactive.tickstatistics.repository.StatisticsRepository;
import com.solactive.tickstatistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final TickValidator tickValidator;

    @Override
    public StatisticsDto getStatistics() {
        Statistics statistics = statisticsRepository.getAggregated();

        return generateDto(statistics);
    }

    @Override
    public StatisticsDto getStatistics(String instrument) {
        Statistics statistics = statisticsRepository.get(instrument);

        return generateDto(statistics);
    }

    private StatisticsDto generateDto(Statistics statistics) {
        StatisticsDto dto = new StatisticsDto();

        //if instrument's last tick arrival is out of sliding time interval, statistics should be empty.
        if(statistics != null &&
            tickValidator.validateTimestamp(statistics.getInstrumentUpdatedAt()))
        {
            dto.setAvg(statistics.getAvg());
            dto.setCount(statistics.getCount());
            dto.setMax(statistics.getMax());
            dto.setMaxDrawdown(statistics.getMaxDrawdown());
            dto.setMin(statistics.getMin());
            dto.setQuantile(statistics.getQuantile());
            dto.setTwap(statistics.getTwap());
            dto.setTwap2(statistics.getTwap2());
            dto.setVolatility(statistics.getVolatility());
        }

        return dto;
    }
}
