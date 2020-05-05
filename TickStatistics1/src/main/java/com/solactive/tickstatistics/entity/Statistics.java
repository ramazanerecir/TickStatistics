package com.solactive.tickstatistics.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Statistics implements Serializable {

    private String instrument;
    private double avg;
    private double max;
    private double min;
    private double maxDrawdown;
    private double volatility;
    private double quantile;
    private double twap;
    private double twap2;
    private long count;

    private long calculatedAt;
    private long instrumentUpdatedAt;

    public Statistics copy()
    {
        Statistics statistics = new Statistics();
        statistics.setInstrument(this.instrument);
        statistics.setAvg(this.avg);
        statistics.setMax(this.max);
        statistics.setMin(min);
        statistics.setMaxDrawdown(maxDrawdown);
        statistics.setVolatility(volatility);
        statistics.setQuantile(this.quantile);
        statistics.setTwap(this.twap);
        statistics.setTwap2(this.twap2);
        statistics.setCount(this.count);
        statistics.setCalculatedAt(this.calculatedAt);
        statistics.setInstrumentUpdatedAt(this.instrumentUpdatedAt);

        return statistics;
    }

}
