package com.solactive.tickstatistics.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TickDto implements Serializable {

    String instrument;
    double price;
    long timestamp;
}
