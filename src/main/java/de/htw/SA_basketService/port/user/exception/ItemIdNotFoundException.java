package de.htw.SA_basketService.port.user.exception;

import java.util.UUID;

public class ItemIdNotFoundException extends RuntimeException{
    public ItemIdNotFoundException(UUID itemId){
        super("Item with id: " + itemId + " was not found in the Basket.");
    }
}
