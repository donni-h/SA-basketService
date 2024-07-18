package de.htw.SA_basketService.unitTests;

import de.htw.SA_basketService.core.domain.service.interfaces.IBasketService;
import de.htw.SA_basketService.port.consumer.BasketConsumer;
import de.htw.SA_basketService.port.dto.CheckoutDto;
import de.htw.SA_basketService.port.user.exception.UsernameNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketConsumerTest {

    @Mock
    private IBasketService mockBasketService;

    @InjectMocks
    private BasketConsumer basketConsumerUnderTest;

    @Test
    void testConsumeMessage() {
        // Setup
        final CheckoutDto checkoutDto = new CheckoutDto();
        checkoutDto.setUsername("username");
        checkoutDto.setStatus("status");

        // Run the test
        basketConsumerUnderTest.consumeMessage(checkoutDto);

        // Verify the results
        verify(mockBasketService).removeAllItemsAfterCheckout("username");
    }

    @Test
    void testConsumeMessage_IBasketServiceThrowsUsernameNotFoundException() {
        // Setup
        final CheckoutDto checkoutDto = new CheckoutDto();
        checkoutDto.setUsername("username");
        checkoutDto.setStatus("status");

        when(mockBasketService.removeAllItemsAfterCheckout("username")).thenThrow(UsernameNotFoundException.class);

        // Run the test
        assertThatThrownBy(() -> basketConsumerUnderTest.consumeMessage(checkoutDto))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
