package de.htw.SA_basketService.core.domain.service.interfaces;

import de.htw.SA_basketService.core.domain.model.Basket;
import de.htw.SA_basketService.core.domain.model.Item;
import de.htw.SA_basketService.port.user.exception.BasketAlreadyExistsException;
import de.htw.SA_basketService.port.user.exception.ItemIdNotFoundException;
import de.htw.SA_basketService.port.user.exception.UsernameNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IBasketService {
    Basket createBasket(String username) throws BasketAlreadyExistsException;
    Basket getBasketByUsername(String username) throws UsernameNotFoundException;
    List<Basket> getAllBaskets();

    Basket addItemToBasket(Item item, String username) throws UsernameNotFoundException;

    Basket removeItemFromBasket(UUID itemId, String username) throws UsernameNotFoundException, ItemIdNotFoundException;
    Basket removeAllItemsAfterCheckout(String username) throws UsernameNotFoundException;

    Basket removeAllItemsFromBasket(String username) throws UsernameNotFoundException;

    void deleteBasket(String username) throws UsernameNotFoundException;
    void deleteAllBaskets();

}
