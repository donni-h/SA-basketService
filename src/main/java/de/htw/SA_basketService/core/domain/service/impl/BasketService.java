package de.htw.SA_basketService.core.domain.service.impl;

import de.htw.SA_basketService.core.domain.model.Basket;
import de.htw.SA_basketService.core.domain.model.Item;
import de.htw.SA_basketService.core.domain.service.interfaces.IBasketRepository;
import de.htw.SA_basketService.core.domain.service.interfaces.IBasketService;
import de.htw.SA_basketService.port.user.exception.BasketAlreadyExistsException;
import de.htw.SA_basketService.port.user.exception.ItemIdNotFoundException;
import de.htw.SA_basketService.port.user.exception.UserIdNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class BasketService implements IBasketService {
    private final IBasketRepository basketRepository;

    @Autowired
    public BasketService(IBasketRepository basketRepository){
        this.basketRepository = basketRepository;
    }

    @Override
    public Basket createBasket(Basket basket) throws BasketAlreadyExistsException{
        UUID userId = basket.getUserId();
        if(basketRepository.existsById(userId)) throw new BasketAlreadyExistsException(userId);
        return basketRepository.save(basket);
    }

    @Override
    public Basket getBasketByUserId(UUID userId) throws UserIdNotFoundException{
        return basketRepository.findById(userId)
                .orElseThrow(() -> new UserIdNotFoundException(userId));
    }

    @Override
    public List<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }

    @Transactional
    @Override
    public Basket addItemToBasket(Item item, UUID userId) throws UserIdNotFoundException{
        Basket existingBasket = getBasketByUserId(userId);
        existingBasket.getItems().add(item);
        updateTotalPrice(existingBasket, item.getItemPrice(), "add");
        return existingBasket;
    }

    @Transactional
    @Override
    public Basket removeItemFromBasket(UUID itemId, UUID userId) throws UserIdNotFoundException,
            ItemIdNotFoundException{
        Basket existingBasket = getBasketByUserId(userId);

        Item itemToDelete = findItemById(existingBasket.getItems(), itemId);
        removeItemFromList(existingBasket.getItems(), itemToDelete);
        updateTotalPrice(existingBasket, itemToDelete.getItemPrice(), "subtract");

        return existingBasket;
    }

    @Transactional
    @Override
    public Basket removeAllItemsFromBasket(UUID userId) throws UserIdNotFoundException{
        Basket existingBasket = getBasketByUserId(userId);
        existingBasket.getItems().clear();
        existingBasket.setTotalPrice(BigDecimal.ZERO);
        return existingBasket;
    }

    @Override
    public void deleteBasket(UUID userId) throws UserIdNotFoundException{
        if(!basketRepository.existsById(userId)) throw new UserIdNotFoundException(userId);
        basketRepository.deleteById(userId);
    }

    @Override
    public void deleteAllBaskets() {
        basketRepository.deleteAll();
    }

    private Item findItemById(List<Item> items, UUID itemId) throws ItemIdNotFoundException {
        return items.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemIdNotFoundException(itemId));
    }

    private void removeItemFromList(List<Item> items, Item itemToDelete) {
        items.remove(itemToDelete);
    }

    private void updateTotalPrice(Basket basket, BigDecimal itemPrice, String mode) {
        BigDecimal currentTotalPrice = basket.getTotalPrice();
        if (mode.equals("add")) basket.setTotalPrice(currentTotalPrice.add(itemPrice));
        if (mode.equals("subtract")) basket.setTotalPrice(currentTotalPrice.subtract(itemPrice));
    }
}
