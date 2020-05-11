package com.solactive.tickstatistics.service.impl;

import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.enums.CalculationType;
import com.solactive.tickstatistics.repository.TickRepository;
import com.solactive.tickstatistics.service.ScheduleService;
import com.solactive.tickstatistics.service.TickEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final TickEventPublisher tickEventPublisher;
    private final TickRepository tickRepository;

    @Override
    @Scheduled(fixedRateString = "${tick.calculation.time.interval}")
    public void recalculateStatistics() {
        tickRepository.getInstrumentList().forEach(this::createTickEvent);
    }

    protected void createTickEvent(String instrument)
    {
        tickEventPublisher.publish(instrument, CalculationType.SCHEDULED);
    }
}
