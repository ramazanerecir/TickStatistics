package com.solactive.tickstatistics.repository.impl;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.Statistics;
import com.solactive.tickstatistics.repository.StatisticsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class StatisticsRepositoryImpl implements StatisticsRepository {

    //in-memory data storage of instrument-statistics
    private Map<String, Statistics> statisticsMap = new ConcurrentHashMap<>();

    @Override
    public Statistics getAggregated() {
        return statisticsMap.get(TickStatisticsConfiguration.aggregatedStatisticsName);
    }

    @Override
    public Statistics get(String instrument) {
        return statisticsMap.get(instrument);
    }

    @Override
    @RabbitListener(queues = "${rabbitmq.statistics.queue.name}")
    public void insert(Statistics statistics) {
        statisticsMap.put(statistics.getInstrument(), statistics);
    }
}
