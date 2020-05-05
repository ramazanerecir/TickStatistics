package com.solactive.tickstatistics.api;

import com.solactive.tickstatistics.entity.dto.StatisticsDto;
import org.springframework.http.ResponseEntity;

public interface StatisticsApi {

    ResponseEntity<StatisticsDto> statistics();

    ResponseEntity<StatisticsDto> statistics(String instrument);
}
