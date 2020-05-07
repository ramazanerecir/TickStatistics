package com.solactive.tickstatistics.api;

import com.solactive.tickstatistics.entity.dto.StatisticsDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

@Api(value = "Statistics API", tags = "statistics")
public interface StatisticsApi {

    @ApiOperation(value = "This is the endpoint with aggregated statistics for all ticks across all instruments.",
            tags = "statistics", produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Statistics of the aggregated ticks in the sliding time interval")})
    ResponseEntity<StatisticsDto> statistics();

    @ApiOperation(value = "This is the endpoint with statistics for a given instrument.",
            tags = "statistics", produces = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Statistics of the instrument ticks in the sliding time interval")})
    ResponseEntity<StatisticsDto> statistics(@ApiParam(value = "instrument", required = true) String instrument);
}
