package com.solactive.tickstatistics.repository;

import com.solactive.tickstatistics.entity.Statistics;

public interface StatisticsRepository {

    Statistics getAggregated();

    Statistics get(String instrument);

    void insert(Statistics statistics);

}
