package de.htw.SA_basketService.core.domain.service.interfaces;

import de.htw.SA_basketService.core.domain.model.Basket;
import de.htw.SA_basketService.core.domain.model.Item;
import de.htw.SA_basketService.port.user.exception.BasketAlreadyExistsException;
import de.htw.SA_basketService.port.user.exception.ItemIdNotFoundException;
import de.htw.SA_basketService.port.user.exception.UserIdNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBasketService {
    Basket createBasket(UUID userId) throws BasketAlreadyExistsException;
    Basket getBasketByUserId(UUID userId) throws UserIdNotFoundException;
    List<Basket> getAllBaskets();

    Basket addItemToBasket(Item item, UUID userId) throws UserIdNotFoundException;

    Basket removeItemFromBasket(UUID itemId, UUID userId) throws UserIdNotFoundException, ItemIdNotFoundException;

    Basket removeAllItemsFromBasket(UUID userId) throws UserIdNotFoundException;

    void deleteBasket(UUID userId) throws UserIdNotFoundException;
    void deleteAllBaskets();

}
