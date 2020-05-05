package com.solactive.tickstatistics.service;

import com.solactive.tickstatistics.entity.InstrumentTick;
import com.solactive.tickstatistics.entity.Statistics;

public interface CalculationService {

    void calculate(InstrumentTick instrumentTick);

    void calculate(InstrumentTick instrumentTick, long calcTimestamp);

    void sendToStatisticsQueue(Statistics statistics);

}
