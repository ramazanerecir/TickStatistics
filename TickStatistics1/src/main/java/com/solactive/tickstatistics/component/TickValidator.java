package com.solactive.tickstatistics.component;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.dto.TickDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TickValidator {

    private final TickStatisticsConfiguration tickStatisticsConfiguration;

    public boolean validateTick(TickDto tickDto) {
        return (!(tickDto.getInstrument() == null ||
                tickDto.getInstrument().isEmpty() ||
                Double.isNaN(tickDto.getPrice()) ||
                Double.isInfinite(tickDto.getPrice()) ||
                tickDto.getPrice() <= 0.0));
    }

    public boolean validateTimestamp(long tickTimestamp) {
        return tickTimestamp >= (System.currentTimeMillis() - tickStatisticsConfiguration.getSlidingTimeInterval());
    }
}
