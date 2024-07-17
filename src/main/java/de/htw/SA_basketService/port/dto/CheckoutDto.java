package de.htw.SA_basketService.port.dto;

import lombok.Data;

@Data
public class CheckoutDto {
    private String username;
    private CheckoutStatus status;
}
