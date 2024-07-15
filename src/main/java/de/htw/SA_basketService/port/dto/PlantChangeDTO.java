package de.htw.SA_basketService.port.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PlantChangeDTO {
    private UUID plantId;
    private int changeAmount;
}
