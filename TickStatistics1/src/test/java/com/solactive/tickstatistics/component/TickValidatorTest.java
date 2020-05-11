package com.solactive.tickstatistics.component;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.dto.TickDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TickValidatorTest {

    @InjectMocks
    private TickValidator tickValidator;

    @Mock
    private TickStatisticsConfiguration tickStatisticsConfiguration;

    @Test
    void validateTick() {
        TickDto tickDto = new TickDto();
        tickDto.setInstrument("IBM.N");
        tickDto.setPrice(100);
        tickDto.setTimestamp(System.currentTimeMillis());

        boolean result = tickValidator.validateTick(tickDto);
        assertTrue(result);
    }

    @Test
    void validateTickNullInstrument() {
        TickDto tickDto = new TickDto();
        tickDto.setPrice(100);
        tickDto.setTimestamp(System.currentTimeMillis());

        boolean result = tickValidator.validateTick(tickDto);
        assertFalse(result);
    }

    @Test
    void validateTickEmptyInstrument() {
        TickDto tickDto = new TickDto();
        tickDto.setInstrument("");
        tickDto.setPrice(100);
        tickDto.setTimestamp(System.currentTimeMillis());

        boolean result = tickValidator.validateTick(tickDto);
        assertFalse(result);
    }

    @Test
    void validateTickNanPrice() {
        TickDto tickDto = new TickDto();
        tickDto.setInstrument("IBM.N");
        tickDto.setPrice(Double.NaN);
        tickDto.setTimestamp(System.currentTimeMillis());

        boolean result = tickValidator.validateTick(tickDto);
        assertFalse(result);
    }

    @Test
    void validateTickInfinitePrice() {
        TickDto tickDto = new TickDto();
        tickDto.setInstrument("IBM.N");
        tickDto.setPrice(Double.POSITIVE_INFINITY);

        tickDto.setTimestamp(System.currentTimeMillis());

        boolean result = tickValidator.validateTick(tickDto);
        assertFalse(result);
    }

    @Test
    void validateTickNegativePrice() {
        TickDto tickDto = new TickDto();
        tickDto.setInstrument("IBM.N");
        tickDto.setPrice(-100);
        tickDto.setTimestamp(System.currentTimeMillis());

        boolean result = tickValidator.validateTick(tickDto);
        assertFalse(result);
    }

    @Test
    void validateTimestamp() {
        boolean result = tickValidator.validateTimestamp(System.currentTimeMillis());
        assertTrue(result);
    }

    @Test
    void validateTimestampOutOfRange() {
        boolean result = tickValidator.validateTimestamp(System.currentTimeMillis()
                - tickStatisticsConfiguration.getSlidingTimeInterval()-1);
        assertFalse(result);
    }
}