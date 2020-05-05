package com.solactive.tickstatistics.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class InstrumentTick implements Serializable {

    String instrument;
    List<Tick> tickList;

    long updatedAt;
    Statistics statistics;

    public InstrumentTick(String instrument)
    {
        this.instrument = instrument;
        this.tickList = new ArrayList<>();
    }
}
