package com.solactive.tickstatistics.service.impl;

import com.solactive.tickstatistics.entity.CalculationEvent;
import com.solactive.tickstatistics.enums.CalculationType;
import com.solactive.tickstatistics.event.TickEventCreated;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TickEventPublisherImplTest {

    @InjectMocks
    TickEventPublisherImpl tickEventPublisher;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Captor
    protected ArgumentCaptor<TickEventCreated> publishEventCaptor;

    @Test
    void publish() {
        String instrument = "IBM.N";

        tickEventPublisher.publish(instrument, CalculationType.SCHEDULED);

        verify(applicationEventPublisher, times(1))
                .publishEvent(publishEventCaptor.capture());

        assertEquals(((CalculationEvent)publishEventCaptor.getValue().getSource()).getInstrument(), instrument);
    }
}