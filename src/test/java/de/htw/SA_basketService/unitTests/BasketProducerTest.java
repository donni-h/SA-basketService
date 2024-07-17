package de.htw.SA_basketService.unitTests;

import de.htw.SA_basketService.port.dto.PlantChangeDTO;
import de.htw.SA_basketService.port.dtomapper.ItemToPlantDTOMapper;
import de.htw.SA_basketService.port.producer.BasketProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketProducerTest {

    @Mock
    private RabbitTemplate mockRabbitTemplate;
    @Mock
    private ItemToPlantDTOMapper mockMapper;

    private BasketProducer basketProducerUnderTest;

    @BeforeEach
    void setUp() {
        basketProducerUnderTest = new BasketProducer(mockRabbitTemplate, mockMapper);
        ReflectionTestUtils.setField(basketProducerUnderTest, "exchange", "basketExchange");
        ReflectionTestUtils.setField(basketProducerUnderTest, "routingKey", "basketToProduct");
    }

    @Test
    void testChangeAmountOfPlant() {
        // Setup
        // Configure ItemToPlantDTOMapper.getPlantChangeDTO(...).
        final PlantChangeDTO plantChangeDTO = new PlantChangeDTO();
        plantChangeDTO.setPlantId(UUID.fromString("ff663b89-0732-441a-959b-4b7f3993d27a"));
        plantChangeDTO.setChangeAmount(0);
        when(mockMapper.getPlantChangeDTO(UUID.fromString("fa10254c-7c28-4232-90a5-bf4abd69610a"), 0))
                .thenReturn(plantChangeDTO);

        // Run the test
        basketProducerUnderTest.changeAmountOfPlant(UUID.fromString("fa10254c-7c28-4232-90a5-bf4abd69610a"), 0);

        // Verify the results
        // Confirm RabbitTemplate.convertAndSend(...).
        final PlantChangeDTO object = new PlantChangeDTO();
        object.setPlantId(UUID.fromString("ff663b89-0732-441a-959b-4b7f3993d27a"));
        object.setChangeAmount(0);
        verify(mockRabbitTemplate).convertAndSend("basketExchange", "basketToProduct", object);
    }

    @Test
    void testChangeAmountOfPlant_RabbitTemplateThrowsAmqpException() {
        // Setup
        // Configure ItemToPlantDTOMapper.getPlantChangeDTO(...).
        final PlantChangeDTO plantChangeDTO = new PlantChangeDTO();
        plantChangeDTO.setPlantId(UUID.fromString("ff663b89-0732-441a-959b-4b7f3993d27a"));
        plantChangeDTO.setChangeAmount(0);
        when(mockMapper.getPlantChangeDTO(UUID.fromString("fa10254c-7c28-4232-90a5-bf4abd69610a"), 0))
                .thenReturn(plantChangeDTO);

        // Configure RabbitTemplate.convertAndSend(...).
        final PlantChangeDTO object = new PlantChangeDTO();
        object.setPlantId(UUID.fromString("ff663b89-0732-441a-959b-4b7f3993d27a"));
        object.setChangeAmount(0);
        doThrow(AmqpException.class).when(mockRabbitTemplate).convertAndSend("basketExchange", "basketToProduct",
                object);

        // Run the test
        assertThatThrownBy(() -> basketProducerUnderTest.changeAmountOfPlant(
                UUID.fromString("fa10254c-7c28-4232-90a5-bf4abd69610a"), 0)).isInstanceOf(AmqpException.class);
    }
}
