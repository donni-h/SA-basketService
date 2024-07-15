package de.htw.SA_basketService.port.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.SA_basketService.port.dto.PlantChangeDTO;
import de.htw.SA_basketService.port.dtomapper.ItemToPlantDTOMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BasketProducer {
    private final RabbitTemplate rabbitTemplate;
    private final ItemToPlantDTOMapper mapper;

    @Value("basket_exchange")
    private String exchange;

    @Value("basket.ToPlant")
    private String routingKey;

    @Autowired
    public BasketProducer(RabbitTemplate rabbitTemplate, ItemToPlantDTOMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    public void changeAmountOfPlant(UUID itemId, int difference){
        PlantChangeDTO plantChangeDTO = mapper.getPlantChangeDTO(itemId, difference);
        ObjectMapper objectMapper = new ObjectMapper();
        String message;
        try {
            message = objectMapper.writeValueAsString(plantChangeDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
