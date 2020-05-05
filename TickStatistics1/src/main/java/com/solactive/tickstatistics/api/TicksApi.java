package com.solactive.tickstatistics.api;

import com.solactive.tickstatistics.entity.dto.TickDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface TicksApi {

    ResponseEntity<Void> tick(@RequestBody TickDto tickDto);
}
