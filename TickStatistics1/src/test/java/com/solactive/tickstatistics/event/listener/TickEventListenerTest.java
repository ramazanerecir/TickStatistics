package com.solactive.tickstatistics.event.listener;

import com.solactive.tickstatistics.entity.CalculationEvent;
import com.solactive.tickstatistics.enums.CalculationType;
import com.solactive.tickstatistics.event.TickEventCreated;
import com.solactive.tickstatistics.repository.StatisticsRepository;
import com.solactive.tickstatistics.repository.TickRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TickEventListenerTest {

    @Spy
    @InjectMocks
    TickEventListener tickEventListener;

    @Mock
    TickRepository tickRepository;

    @Mock
    StatisticsRepository statisticsRepository;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Captor
    ArgumentCaptor<CalculationEvent> calculationEventCaptor;

    @Test
    void listenTickEvent() {
        CalculationEvent calculationEvent = new CalculationEvent("IBM.N", CalculationType.SCHEDULED);
        TickEventCreated tickEventCreated = new TickEventCreated(calculationEvent);

        tickEventListener.listenTickEvent(tickEventCreated);

        verify(tickEventListener, times(1))
                .sendToCalculationQueue(calculationEventCaptor.capture());

        assertEquals(calculationEventCaptor.getValue(), calculationEvent);
    }

    @Test
    public void sendToCalculationQueue() {
        CalculationEvent calculationEvent = new CalculationEvent("IBM.N", CalculationType.SCHEDULED);

        //when()
        tickEventListener.sendToCalculationQueue(calculationEvent);

    }
}