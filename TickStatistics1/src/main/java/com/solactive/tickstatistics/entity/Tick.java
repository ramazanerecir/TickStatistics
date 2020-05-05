package com.solactive.tickstatistics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Tick implements Serializable {

    double price;
    long timestamp;
}
