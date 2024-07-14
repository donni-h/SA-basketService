package de.htw.SA_basketService.port.user.exception;

import java.util.UUID;

public class BasketAlreadyExistsException extends RuntimeException{
    public BasketAlreadyExistsException(UUID userId){
        super("Basket for user with id: " + userId + " already exists.");
    }
}
