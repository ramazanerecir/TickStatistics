package com.solactive.tickstatistics.event;

import org.springframework.context.ApplicationEvent;

public class TickEventCreated extends ApplicationEvent {

    public TickEventCreated(Object source) {
        super(source);
    }
}
