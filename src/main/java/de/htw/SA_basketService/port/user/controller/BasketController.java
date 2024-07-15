package de.htw.SA_basketService.port.user.controller;

import de.htw.SA_basketService.core.domain.model.Basket;
import de.htw.SA_basketService.core.domain.model.Item;
import de.htw.SA_basketService.core.domain.service.interfaces.IBasketRepository;
import de.htw.SA_basketService.core.domain.service.interfaces.IBasketService;
import de.htw.SA_basketService.port.producer.BasketProducer;
import de.htw.SA_basketService.port.user.exception.BasketAlreadyExistsException;
import de.htw.SA_basketService.port.user.exception.ItemIdNotFoundException;
import de.htw.SA_basketService.port.user.exception.UserIdNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
public class BasketController {
    private final IBasketService basketService;
    @Autowired
    public BasketController(IBasketService basketService){
        this.basketService = basketService;
    }

    @PostMapping(path = "/basket/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Basket createBasket(
            @PathVariable("userId")
            @NotNull(message = "userId cannot be null")
            UUID userId) throws BasketAlreadyExistsException {
        return basketService.createBasket(userId);
    }

    @GetMapping(path = "/basket/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Basket getBasketByUserId(
            @PathVariable("userId")
            @NotNull(message = "userId cannot be null")
            UUID userId) throws UserIdNotFoundException {
        return basketService.getBasketByUserId(userId);
    }

    @GetMapping(path = "/baskets")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Basket> getAllBaskets(){
        return basketService.getAllBaskets();
    }

    @PutMapping(path = "/basket/additem/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Basket addItemToBasket(
            @Valid
            @RequestBody
            Item item,
            @PathVariable("userId")
            @NotNull(message = "userId cannot be null")
            UUID userId) throws UserIdNotFoundException {
        return basketService.addItemToBasket(item, userId);
    }

    @PutMapping(path = "/basket/{userId}/removeitem/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Basket removeItemFromBasket(
            @PathVariable("itemId")
            @NotNull(message = "itemId cannot be null")
            UUID itemId,
            @PathVariable("userId")
            @NotNull(message = "userId cannot be null")
            UUID userId) throws UserIdNotFoundException, ItemIdNotFoundException{
        return basketService.removeItemFromBasket(itemId, userId);
    }
    @PutMapping(path = "/basket/removeallitems/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Basket removeAllItemsFromBasket(
            @PathVariable("userId")
            @NotNull(message = "userId cannot be null")
            UUID userId) throws UserIdNotFoundException{
        return basketService.removeAllItemsFromBasket(userId);
    }
    @DeleteMapping(path = "/basket/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBasket(
            @PathVariable("userId")
            @NotNull(message = "userId cannot be null")
            UUID userId) throws UserIdNotFoundException{
        basketService.deleteBasket(userId);
    }

    @DeleteMapping(path = "/baskets")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllBaskets(){
        basketService.deleteAllBaskets();
    }

}
