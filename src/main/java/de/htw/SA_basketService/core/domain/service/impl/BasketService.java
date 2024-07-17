package de.htw.SA_basketService.core.domain.service.impl;

import de.htw.SA_basketService.core.domain.model.Basket;
import de.htw.SA_basketService.core.domain.model.Item;
import de.htw.SA_basketService.core.domain.service.interfaces.IBasketRepository;
import de.htw.SA_basketService.core.domain.service.interfaces.IBasketService;
import de.htw.SA_basketService.port.producer.BasketProducer;
import de.htw.SA_basketService.port.user.exception.BasketAlreadyExistsException;
import de.htw.SA_basketService.port.user.exception.ItemIdNotFoundException;
import de.htw.SA_basketService.port.user.exception.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class BasketService implements IBasketService {
    private final IBasketRepository basketRepository;
    private final BasketProducer basketProducer;

    @Autowired
    public BasketService(IBasketRepository basketRepository, BasketProducer basketProducer){
        this.basketRepository = basketRepository;
        this.basketProducer = basketProducer;
    }

    @Override
    public Basket createBasket(String username) throws BasketAlreadyExistsException{
        if(basketRepository.existsById(username)) throw new BasketAlreadyExistsException(username);
        Basket basket = new Basket(username);
        return basketRepository.save(basket);
    }

    @Override
    public Basket getBasketByUsername(String username) throws UsernameNotFoundException {
        return basketRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public List<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }

    @Transactional
    @Override
    public Basket addItemToBasket(Item item, String username) throws UsernameNotFoundException {
        Basket existingBasket = getBasketByUsername(username);
        existingBasket.getItems().add(item);
        updateTotalPrice(existingBasket, item.getItemPrice(), "add");
        return existingBasket;
    }

    @Transactional
    @Override
    public Basket removeItemFromBasket(UUID itemId, String username) throws UsernameNotFoundException,
            ItemIdNotFoundException{
        Basket existingBasket = getBasketByUsername(username);

        Item itemToDelete = findItemById(existingBasket.getItems(), itemId);
        existingBasket.getItems().remove(itemToDelete);
        updateTotalPrice(existingBasket, itemToDelete.getItemPrice(), "subtract");

        return existingBasket;
    }

    @Transactional
    @Override
    public Basket removeAllItemsFromBasket(String username) throws UsernameNotFoundException {
        Basket existingBasket = getBasketByUsername(username);
        notifyPlantService(existingBasket.getItems(), 1);
        existingBasket.getItems().clear();
        existingBasket.setTotalPrice(BigDecimal.ZERO);
        return existingBasket;
    }

    @Transactional
    @Override
    public Basket removeAllItemsAfterCheckout(String username) throws UsernameNotFoundException {
        Basket existingBasket = getBasketByUsername(username);
        notifyPlantService(existingBasket.getItems(), -1);
        existingBasket.getItems().clear();
        existingBasket.setTotalPrice(BigDecimal.ZERO);
        return existingBasket;
    }

    @Override
    public void deleteBasket(String username) throws UsernameNotFoundException {
        Basket existingBasket = getBasketByUsername(username);
        notifyPlantService(existingBasket.getItems(), 1);
        basketRepository.deleteById(username);
    }

    @Override
    public void deleteAllBaskets() {
        List<Basket> baskets = basketRepository.findAll();
        for (Basket basket: baskets){
            notifyPlantService(basket.getItems(), 1);
        }
        basketRepository.deleteAll();
    }

    private Item findItemById(List<Item> items, UUID itemId) throws ItemIdNotFoundException {
        return items.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemIdNotFoundException(itemId));
    }

    private void updateTotalPrice(Basket basket, BigDecimal itemPrice, String mode) {
        BigDecimal currentTotalPrice = basket.getTotalPrice();
        if (mode.equals("add")) basket.setTotalPrice(currentTotalPrice.add(itemPrice));
        if (mode.equals("subtract")) basket.setTotalPrice(currentTotalPrice.subtract(itemPrice));
    }

    private void notifyPlantService(List<Item> items, int difference){
        for (Item item: items){
            basketProducer.changeAmountOfPlant(item.getPlantId(), difference);
        }
    }
}
