package com.solactive.tickstatistics.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TickStatisticsConfiguration {

    public static final String aggregatedStatisticsName = "AGGREGATED_STATISTICS";

    @Value("${tick.sliding.time.interval}")
    private long slidingTimeInterval;

    @Value("${tick.calculation.time.interval}")
    private long calculationTimeInterval;

    @Value("${statistics.lambda}")
    private double lambda;

    @Value("${statistics.percentile}")
    private double percentile;

    @Value("${statistics.scale}")
    private int scale;

    @Value("${statistics.price.scale}")
    private int priceScale;
}
