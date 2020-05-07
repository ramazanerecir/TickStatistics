package com.solactive.tickstatistics.api;

import com.solactive.tickstatistics.entity.dto.TickDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

@Api(value = "Ticks API", tags = "ticks")
public interface TicksApi {

    @ApiOperation(value = "Every time a new tick arrives, this endpoint will be called."
            , tags = "ticks", consumes = "application/json")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Tick is created"),
            @ApiResponse(code = 204, message = "Tick is not valid") })
    ResponseEntity<Void> tick(@ApiParam(name = "Tick", value = "New tick", required = true) TickDto tickDto);
}
