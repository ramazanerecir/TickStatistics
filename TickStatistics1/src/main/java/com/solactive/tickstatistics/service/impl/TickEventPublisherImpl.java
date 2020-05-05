package com.solactive.tickstatistics.service.impl;

import com.solactive.tickstatistics.entity.CalculationEvent;
import com.solactive.tickstatistics.enums.CalculationType;
import com.solactive.tickstatistics.event.TickEventCreated;
import com.solactive.tickstatistics.service.TickEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TickEventPublisherImpl implements TickEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Async
    public void publish(String instrument, CalculationType calculationType) {
        applicationEventPublisher.publishEvent(new TickEventCreated(new CalculationEvent(instrument, calculationType)));
    }
}
