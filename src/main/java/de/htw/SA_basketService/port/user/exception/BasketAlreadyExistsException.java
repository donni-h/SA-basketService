package de.htw.SA_basketService.port.user.exception;

import java.util.UUID;

public class BasketAlreadyExistsException extends RuntimeException{
    public BasketAlreadyExistsException(String username){
        super("Basket for user with username: " + username + " already exists.");
    }
}
