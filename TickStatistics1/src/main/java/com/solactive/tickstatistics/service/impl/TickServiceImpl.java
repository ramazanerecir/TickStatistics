package com.solactive.tickstatistics.service.impl;

import com.solactive.tickstatistics.entity.dto.TickDto;
import com.solactive.tickstatistics.service.TickService;
import com.solactive.tickstatistics.component.TickValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TickServiceImpl implements TickService {

    private final RabbitTemplate rabbitTemplate;
    private final TickValidator tickValidator;

    @Value("${rabbitmq.tick.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.tick.routing.name}")
    private String routingName;

    @Override
    public boolean insertTick(TickDto tickDto) {
        if(tickValidator.validateTick(tickDto) &&
                tickValidator.validateTimestamp(tickDto.getTimestamp()))
        {
            sendToTickQueue(tickDto);
            return true;
        }
        return false;
    }

    @Override
    public void sendToTickQueue(TickDto tickDto) {
        rabbitTemplate.convertAndSend(exchangeName, routingName, tickDto);
    }

}
