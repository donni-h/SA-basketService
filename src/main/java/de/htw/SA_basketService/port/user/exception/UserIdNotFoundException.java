package de.htw.SA_basketService.port.user.exception;

import java.util.UUID;

public class UserIdNotFoundException extends RuntimeException{
    public UserIdNotFoundException(UUID userId){
        super("Basket for user with id: " + userId + " could not be found.");
    }
}
