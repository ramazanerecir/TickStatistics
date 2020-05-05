package com.solactive.tickstatistics.repository.impl;

import com.solactive.tickstatistics.component.TickValidator;
import com.solactive.tickstatistics.configuration.TickStatisticsConfiguration;
import com.solactive.tickstatistics.entity.InstrumentTick;
import com.solactive.tickstatistics.entity.Tick;
import com.solactive.tickstatistics.entity.dto.TickDto;
import com.solactive.tickstatistics.enums.CalculationType;
import com.solactive.tickstatistics.service.TickEventPublisher;
import com.solactive.tickstatistics.repository.TickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TickRepositoryImpl implements TickRepository {

    private final TickEventPublisher tickEventPublisher;
    private final TickValidator tickValidator;

    //in-memory data storage of instrument-ticks
    private Map<String, InstrumentTick> instrumentMap = new ConcurrentHashMap<>();

    /*
    * Listens Tick-Queue
    * Inserts Tick to memory
    * Creates tick events to trigger sending ticks to calculation queue for statistics calculation
    * */
    @Override
    @RabbitListener(queues = "${rabbitmq.tick.queue.name}")
    public void insert(TickDto tickDto)
    {
        insertInstrumentTick(tickDto.getInstrument(), new Tick(tickDto.getPrice(), tickDto.getTimestamp()));
        createTickEvent(tickDto.getInstrument());
    }

    private void insertInstrumentTick(String instrument, Tick tick)
    {
        instrumentMap.putIfAbsent(instrument, new InstrumentTick(instrument));
        instrumentMap.get(instrument).getTickList().add(tick);
        instrumentMap.get(instrument).setUpdatedAt(new Timestamp(new Date().getTime()).getTime());

    }

    protected void createTickEvent(String instrument)
    {
        tickEventPublisher.publish(instrument, CalculationType.NEWTICK);
        tickEventPublisher.publish(TickStatisticsConfiguration.aggregatedStatisticsName, CalculationType.NEWTICK);
    }

    @Override
    public InstrumentTick getFilteredInstrumentTick(String instrument)
    {
        InstrumentTick instrumentTick;
        if(!instrumentMap.containsKey(instrument))
        {
            instrumentTick = new InstrumentTick(instrument);
        }
        else {
            instrumentTick = new InstrumentTick();
            instrumentTick.setInstrument(instrument);
            instrumentTick.setUpdatedAt(instrumentMap.get(instrument).getUpdatedAt());
            instrumentTick.setTickList(instrumentMap.get(instrument).getTickList()
                    .parallelStream()
                    .filter(t -> tickValidator.validateTimestamp(t.getTimestamp()))
                    .collect(Collectors.toList()));
        }
        return instrumentTick;
    }

    @Override
    public InstrumentTick getFilteredAllTicks()
    {
        InstrumentTick instrumentTick = new InstrumentTick();
        List<Tick> tickList = instrumentMap.values()
                .parallelStream()
                .map(InstrumentTick::getTickList)
                .flatMap(List::stream)
                .filter(t -> tickValidator.validateTimestamp(t.getTimestamp()))
                .collect(Collectors.toList());

        instrumentTick.setInstrument(TickStatisticsConfiguration.aggregatedStatisticsName);
        instrumentTick.setUpdatedAt(instrumentMap.values()
                .parallelStream()
                .mapToLong(InstrumentTick::getUpdatedAt)
                .max().orElse(0));
        instrumentTick.setTickList(tickList);
        return instrumentTick;
    }

    @Override
    public List<String> getInstrumentList()
    {
        return new ArrayList<>(instrumentMap.keySet());
    }

}
