package de.htw.SA_basketService.port.consumer;

import de.htw.SA_basketService.core.domain.service.interfaces.IBasketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BasketConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasketConsumer.class);

    @Autowired
    private IBasketService basketService;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consumeMessage() {
        LOGGER.info("Message From Checkout Received");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            basketService.removeAllItemsAfterCheckout(username);
        } else {
            LOGGER.warn("Authentication object is null, cannot remove items from basket");
        }
    }
}
