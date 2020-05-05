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
}
