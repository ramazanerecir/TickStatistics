package com.solactive.tickstatistics.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "Tick", description = "Tick price and timestamp info of an instrument" )
public class TickDto implements Serializable {

    @ApiModelProperty(value = "a financial instrument identifier")
    String instrument;

    @ApiModelProperty(value = "current trade price of a financial instrument")
    double price;

    @ApiModelProperty(value = "tick timestamp in milliseconds")
    long timestamp;
}
