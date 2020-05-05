package com.solactive.tickstatistics.repository;

import com.solactive.tickstatistics.entity.InstrumentTick;
import com.solactive.tickstatistics.entity.dto.TickDto;

import java.util.List;

public interface TickRepository {

    void insert(TickDto tickDto);

    List<String> getInstrumentList();

    InstrumentTick getFilteredInstrumentTick(String instrument);

    InstrumentTick getFilteredAllTicks();

}
