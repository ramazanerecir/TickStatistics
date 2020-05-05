package com.solactive.tickstatistics.service;

import com.solactive.tickstatistics.enums.CalculationType;

public interface TickEventPublisher {

    void publish(String instrument, CalculationType calculationType);

}
