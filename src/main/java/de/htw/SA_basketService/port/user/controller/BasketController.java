package de.htw.SA_basketService.port.user.controller;

import de.htw.SA_basketService.core.domain.model.Basket;
import de.htw.SA_basketService.core.domain.model.Item;
import de.htw.SA_basketService.core.domain.service.interfaces.IBasketService;
import de.htw.SA_basketService.port.producer.BasketProducer;
import de.htw.SA_basketService.port.user.exception.BasketAlreadyExistsException;
import de.htw.SA_basketService.port.user.exception.ItemIdNotFoundException;
import de.htw.SA_basketService.port.user.exception.UsernameNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
public class BasketController {
    private final IBasketService basketService;
    private final BasketProducer basketProducer;
    @Autowired
    public BasketController(IBasketService basketService, BasketProducer basketProducer){
        this.basketService = basketService;
        this.basketProducer = basketProducer;
    }

    @PostMapping(path = "/basket")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Basket createBasket(Authentication connectedUser) throws BasketAlreadyExistsException {
        return basketService.createBasket(connectedUser.getName());
    }

    @GetMapping(path = "/basket")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Basket getBasketByUsername(Authentication connectedUser) throws UsernameNotFoundException {
        return basketService.getBasketByUsername(connectedUser.getName());
    }

    @GetMapping(path = "/baskets")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Basket> getAllBaskets(){
        return basketService.getAllBaskets();
    }

    @PutMapping(path = "/basket/additem")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Basket addItemToBasket(
            @Valid
            @RequestBody
            Item item,
            Authentication connectedUser) throws UsernameNotFoundException {
        basketProducer.changeAmountOfPlant(item.getItemId(), -1);
        return basketService.addItemToBasket(item, connectedUser.getName());
    }

    @PutMapping(path = "/basket/removeitem/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Basket removeItemFromBasket(
            @PathVariable("itemId")
            @NotNull(message = "itemId cannot be null")
            UUID itemId,
            Authentication connectedUser) throws UsernameNotFoundException, ItemIdNotFoundException{
        basketProducer.changeAmountOfPlant(itemId, 1);
        return basketService.removeItemFromBasket(itemId, connectedUser.getName());
    }
    @PutMapping(path = "/basket/removeallitems")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Basket removeAllItemsFromBasket(Authentication connectedUser) throws UsernameNotFoundException {
        return basketService.removeAllItemsFromBasket(connectedUser.getName());
    }
    @DeleteMapping(path = "/basket")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBasket(Authentication connectedUser) throws UsernameNotFoundException {
        basketService.deleteBasket(connectedUser.getName());
    }

    @DeleteMapping(path = "/baskets")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllBaskets(){
        basketService.deleteAllBaskets();
    }

}
