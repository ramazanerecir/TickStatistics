package com.solactive.tickstatistics.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatisticsDto {

    private double avg;
    private double max;
    private double min;
    private double maxDrawdown;
    private double volatility;
    private double quantile;
    private double twap;
    private double twap2;
    private long count;
}
