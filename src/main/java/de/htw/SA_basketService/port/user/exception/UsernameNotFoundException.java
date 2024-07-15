package de.htw.SA_basketService.port.user.exception;

import java.util.UUID;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String username){
        super("Basket for user with username: " + username + " could not be found.");
    }
}
