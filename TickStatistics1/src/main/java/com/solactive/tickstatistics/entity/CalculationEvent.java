package com.solactive.tickstatistics.entity;

import com.solactive.tickstatistics.enums.CalculationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationEvent implements Serializable {

    String instrument;
    CalculationType calculationType;
}
