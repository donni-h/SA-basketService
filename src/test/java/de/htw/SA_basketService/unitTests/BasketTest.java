package de.htw.SA_basketService.unitTests;

import de.htw.SA_basketService.core.domain.model.Basket;
import de.htw.SA_basketService.core.domain.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BasketTest {

    @Mock
    private List<Item> mockItems;
    @Mock
    private Item item1;
    @Mock
    private Item item2;
    @Mock
    private Item item3;
    private Basket basketUnderTest;

    @BeforeEach
    void setUp() {
        basketUnderTest = new Basket("username", mockItems, BigDecimal.TEN);
    }

    @Test
    void testEqualsTrue() {
        // Setup
        Basket referenceBasket = new Basket("username", mockItems, BigDecimal.TEN);
        when(mockItems.iterator()).thenReturn(Arrays.asList(item1, item2, item3).iterator());
        when(mockItems.contains(any(Item.class))).thenReturn(true);

        // Run the test
        final boolean result = basketUnderTest.equals(referenceBasket);

        // Verify the results
        assertThat(result).isTrue();
    }

    @Test
    void testEqualsFalse() {
        // Setup
        Basket referenceBasket = new Basket("username", mockItems, BigDecimal.ZERO);
        when(mockItems.iterator()).thenReturn(Arrays.asList(item1, item2, item3).iterator());
        when(mockItems.contains(any(Item.class))).thenReturn(true);

        // Run the test
        final boolean result = basketUnderTest.equals(referenceBasket);

        // Verify the results
        assertThat(result).isFalse();
    }

    @Test
    void testUsernameGetterAndSetter() {
        final String username = "username";
        basketUnderTest.setUsername(username);
        assertThat(basketUnderTest.getUsername()).isEqualTo(username);
    }

    @Test
    void testGetItems() {
        assertThat(basketUnderTest.getItems()).isEqualTo(mockItems);
    }

    @Test
    void testTotalPriceGetterAndSetter() {
        final BigDecimal totalPrice = new BigDecimal("0.00");
        basketUnderTest.setTotalPrice(totalPrice);
        assertThat(basketUnderTest.getTotalPrice()).isEqualTo(totalPrice);
    }
}
