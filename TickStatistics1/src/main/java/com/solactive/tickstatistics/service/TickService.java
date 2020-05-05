package com.solactive.tickstatistics.service;

import com.solactive.tickstatistics.entity.dto.TickDto;

public interface TickService {

    boolean insertTick(TickDto tickDto);

    void sendToTickQueue(TickDto tickDto);
}
