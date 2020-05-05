package com.solactive.tickstatistics.service;

public interface TickEventPublisher {

    void publish(String instrument);

}
