package de.htw.SA_basketService.port.producer;

import de.htw.SA_basketService.port.dto.PlantChangeDTO;
import de.htw.SA_basketService.port.dtomapper.ItemToPlantDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BasketProducer {
    private final RabbitTemplate rabbitTemplate;
    private final ItemToPlantDTOMapper mapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketProducer.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routingKey.name}")
    private String routingKey;

    @Autowired
    public BasketProducer(RabbitTemplate rabbitTemplate, ItemToPlantDTOMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    public void changeAmountOfPlant(UUID itemId, int difference){
        PlantChangeDTO plantChangeDTO = mapper.getPlantChangeDTO(itemId, difference);
        LOGGER.info(String.format("sent message -> %s", plantChangeDTO.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, plantChangeDTO);
    }
}
