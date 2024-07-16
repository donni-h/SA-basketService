package de.htw.SA_basketService.unitTests;

import de.htw.SA_basketService.core.domain.model.Basket;
import de.htw.SA_basketService.core.domain.model.Item;
import de.htw.SA_basketService.core.domain.service.impl.BasketService;
import de.htw.SA_basketService.core.domain.service.interfaces.IBasketRepository;
import de.htw.SA_basketService.port.producer.BasketProducer;
import de.htw.SA_basketService.port.user.exception.BasketAlreadyExistsException;
import de.htw.SA_basketService.port.user.exception.UsernameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private IBasketRepository mockBasketRepository;
    @Mock
    private BasketProducer mockBasketProducer;

    private BasketService basketServiceUnderTest;

    @BeforeEach
    void setUp() {
        basketServiceUnderTest = new BasketService(mockBasketRepository, mockBasketProducer);
    }

    @Test
    void testCreateBasket() {
        // Setup
        Basket testBasket = new Basket("username");
        when(mockBasketRepository.existsById("username")).thenReturn(false);
        when(mockBasketRepository.save(any(Basket.class))).thenReturn(testBasket);

        // Run the test
        final Basket result = basketServiceUnderTest.createBasket("username");

        // Verify the results
        assertThat(result).isEqualTo(testBasket);
    }

    @Test
    void testCreateBasket_IBasketRepositoryExistsByIdReturnsTrue() {
        // Setup
        when(mockBasketRepository.existsById("username")).thenReturn(true);

        // Run the test
        assertThatThrownBy(() -> basketServiceUnderTest.createBasket("username"))
                .isInstanceOf(BasketAlreadyExistsException.class);
    }

    @Test
    void testGetBasketByUsername() {
        // Setup
        Basket testBasket = new Basket("username");
        when(mockBasketRepository.findById("username")).thenReturn(Optional.of(testBasket));

        // Run the test
        final Basket result = basketServiceUnderTest.getBasketByUsername("username");

        // Verify the results
        assertThat(result).isEqualTo(testBasket);
    }

    @Test
    void testGetBasketByUsername_IBasketRepositoryReturnsAbsent() {
        // Setup
        when(mockBasketRepository.findById("username")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> basketServiceUnderTest.getBasketByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void testGetAllBaskets() {
        // Setup
        Basket testBasket1 = new Basket("username1");
        Basket testBasket2 = new Basket("username2");
        List<Basket> testBaskets = List.of(testBasket1, testBasket2);
        when(mockBasketRepository.findAll()).thenReturn(testBaskets);

        // Run the test
        final List<Basket> result = basketServiceUnderTest.getAllBaskets();

        // Verify the results
        assertThat(result).isEqualTo(testBaskets);
    }

    @Test
    void testGetAllBaskets_IBasketRepositoryReturnsNoItems() {
        // Setup
        when(mockBasketRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<Basket> result = basketServiceUnderTest.getAllBaskets();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testAddItemToBasket_IBasketRepositoryReturnsAbsent() {
        // Setup
        final Item item = new Item(UUID.fromString("7c6586c5-c2a8-45f1-b6bf-b97ad5f1b6bb"),
                UUID.fromString("8910182e-5f81-4fdf-a5ec-5a3a2035e019"), "name", new BigDecimal("0.00"), "imageLink");
        when(mockBasketRepository.findById("username")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> basketServiceUnderTest.addItemToBasket(item, "username"))
                .isInstanceOf(UsernameNotFoundException.class);
    }



    @Test
    void testRemoveItemFromBasket_IBasketRepositoryReturnsAbsent() {
        // Setup
        when(mockBasketRepository.findById("username")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> basketServiceUnderTest.removeItemFromBasket(
                UUID.fromString("2769f594-1725-409b-905a-35a3f4096441"), "username"))
                .isInstanceOf(UsernameNotFoundException.class);
    }



    @Test
    void testRemoveAllItemsFromBasket_IBasketRepositoryReturnsAbsent() {
        // Setup
        when(mockBasketRepository.findById("username")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> basketServiceUnderTest.removeAllItemsFromBasket("username"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void testDeleteBasket() {
        // Setup
        // Configure IBasketRepository.findById(...).
        final Basket basket1 = new Basket();
        basket1.setUsername("username");
        final Item item = new Item();
        item.setItemId(UUID.fromString("5c8f8ca2-45bd-42a5-9236-dfe6fb2b4cda"));
        item.setPlantId(UUID.fromString("8910182e-5f81-4fdf-a5ec-5a3a2035e019"));
        item.setItemPrice(new BigDecimal("0.00"));
        basket1.setItems(List.of(item));
        basket1.setTotalPrice(new BigDecimal("0.00"));
        final Optional<Basket> basket = Optional.of(basket1);
        when(mockBasketRepository.findById("username")).thenReturn(basket);

        // Run the test
        basketServiceUnderTest.deleteBasket("username");

        // Verify the results
        verify(mockBasketProducer).changeAmountOfPlant(UUID.fromString("8910182e-5f81-4fdf-a5ec-5a3a2035e019"), 1);
        verify(mockBasketRepository).deleteById("username");
    }

    @Test
    void testDeleteBasket_IBasketRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockBasketRepository.findById("username")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> basketServiceUnderTest.deleteBasket("username"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void testDeleteAllBaskets() {
        // Setup
        // Configure IBasketRepository.findAll(...).
        final Basket basket = new Basket();
        basket.setUsername("username");
        final Item item = new Item();
        item.setItemId(UUID.fromString("5c8f8ca2-45bd-42a5-9236-dfe6fb2b4cda"));
        item.setPlantId(UUID.fromString("8910182e-5f81-4fdf-a5ec-5a3a2035e019"));
        item.setItemPrice(new BigDecimal("0.00"));
        basket.setItems(List.of(item));
        basket.setTotalPrice(new BigDecimal("0.00"));
        final List<Basket> baskets = List.of(basket);
        when(mockBasketRepository.findAll()).thenReturn(baskets);

        // Run the test
        basketServiceUnderTest.deleteAllBaskets();

        // Verify the results
        verify(mockBasketProducer).changeAmountOfPlant(UUID.fromString("8910182e-5f81-4fdf-a5ec-5a3a2035e019"), 1);
        verify(mockBasketRepository).deleteAll();
    }

    @Test
    void testDeleteAllBaskets_IBasketRepositoryFindAllReturnsNoItems() {
        // Setup
        when(mockBasketRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        basketServiceUnderTest.deleteAllBaskets();

        // Verify the results
        verify(mockBasketRepository).deleteAll();
    }
}
