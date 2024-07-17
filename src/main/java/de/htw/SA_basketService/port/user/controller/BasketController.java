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
        System.out.println(connectedUser.getName());
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
        if (item.getItemId()!=null) throw new IllegalArgumentException("Item ID is created automatically and " +
                "should not be given.");
        Basket basket = basketService.addItemToBasket(item, connectedUser.getName());
        basketProducer.changeAmountOfPlant(item.getPlantId(), -1);
        return basket;
    }

    @PutMapping(path = "/basket/removeitem")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Basket removeItemFromBasket(
            @Valid
            @RequestBody
            Item item,
            Authentication connectedUser) throws UsernameNotFoundException, ItemIdNotFoundException{

        Basket basket = basketService.removeItemFromBasket(item.getItemId(), connectedUser.getName());
        basketProducer.changeAmountOfPlant(item.getPlantId(), 1);
        return basket;
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

    @GetMapping(path="/basket/test")
    @ResponseStatus(HttpStatus.OK)
    public void testBasket(){
        var uuid = UUID.randomUUID();
        basketProducer.changeAmountOfPlant(uuid, 1);
    }

}
