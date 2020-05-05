package com.solactive.tickstatistics.service;

import com.solactive.tickstatistics.entity.dto.StatisticsDto;

public interface StatisticsService {

    StatisticsDto getStatistics();

    StatisticsDto getStatistics(String instrument);
}
