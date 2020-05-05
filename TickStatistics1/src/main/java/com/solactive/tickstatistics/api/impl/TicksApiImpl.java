package com.solactive.tickstatistics.api.impl;

import com.solactive.tickstatistics.api.TicksApi;
import com.solactive.tickstatistics.entity.dto.TickDto;
import com.solactive.tickstatistics.service.TickService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("ticks")
@RequiredArgsConstructor
public class TicksApiImpl implements TicksApi {

    private final TickService tickService;

    @PostMapping
    public ResponseEntity<Void> tick(@RequestBody TickDto tickDto)
    {
        if(tickService.insertTick(tickDto)) {
            return ResponseEntity.created(URI.create("")).build();
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }
}
