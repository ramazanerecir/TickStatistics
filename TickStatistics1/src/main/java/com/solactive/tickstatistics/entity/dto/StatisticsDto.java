package com.solactive.tickstatistics.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "Statistics", description = "Statistics info in the sliding time interval" )
public class StatisticsDto {

    @ApiModelProperty(value = "specifying the average amount of all tick prices in the sliding time interval")
    private double avg;

    @ApiModelProperty(value = "specifying single highest tick price in the sliding time interval")
    private double max;

    @ApiModelProperty(value = "specifying single lowest tick price in the sliding time interval")
    private double min;

    @ApiModelProperty(value = "specifying the maximum drawdown in the sliding time interval")
    private double maxDrawdown;

    @ApiModelProperty(value = "specifying the volatility in the sliding time interval")
    private double volatility;

    @ApiModelProperty(value = "specifying the lower 5% percentile of the ticks in the sliding time interval")
    private double quantile;

    @ApiModelProperty(value = "specifying the time weighted average of the ticks in the sliding time interval")
    private double twap;

    @ApiModelProperty(value = "specifying the time weighted average of the ticks in the sliding time interval")
    private double twap2;

    @ApiModelProperty(value = "specifying the total number of ticks happened in the sliding time interval")
    private long count;
}
