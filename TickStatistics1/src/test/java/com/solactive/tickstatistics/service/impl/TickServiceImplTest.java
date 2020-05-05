package com.solactive.tickstatistics.service.impl;

import com.solactive.tickstatistics.component.TickValidator;
import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.dto.TickDto;
import com.solactive.tickstatistics.event.TickEventCreated;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TickServiceImplTest {

    @Spy
    @InjectMocks
    private TickServiceImpl tickService;

    @Mock
    private TickValidator tickValidator;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Captor
    protected ArgumentCaptor<TickDto> tickDtoCaptor;

    @Captor
    protected ArgumentCaptor<String> stringCaptor;

    @Test
    void insertTick() {
        TickDto tickDto = new TickDto();
        tickDto.setInstrument("IBM.N");
        tickDto.setPrice(100);
        tickDto.setTimestamp(new Timestamp(new Date().getTime()).getTime());

        when(tickValidator.validateTick(tickDto)).thenReturn(true);
        when(tickValidator.validateTimestamp(tickDto.getTimestamp())).thenReturn(true);
        boolean result = tickService.insertTick(tickDto);

        verify(tickService, times(1))
                .sendToTickQueue(tickDtoCaptor.capture());

        assertTrue(result);
        assertEquals(tickDtoCaptor.getValue().getInstrument(), tickDto.getInstrument());


    }

    @Test
    void insertTickNotValidTick() {
        TickDto tickDto = new TickDto();
        tickDto.setPrice(100);
        tickDto.setTimestamp(new Timestamp(new Date().getTime()).getTime());

        when(tickValidator.validateTick(tickDto)).thenReturn(false);
        boolean result = tickService.insertTick(tickDto);

        assertFalse(result);
    }

    @Test
    void sendToTickQueue() {
        TickDto tickDto = new TickDto();
        tickDto.setInstrument("IBM.N");
        tickDto.setPrice(100);
        tickDto.setTimestamp(new Timestamp(new Date().getTime()).getTime());

        tickService.sendToTickQueue(tickDto);
        verify(rabbitTemplate).convertAndSend(stringCaptor.capture(), stringCaptor.capture(),
                Mockito.any(TickDto.class));
    }
}