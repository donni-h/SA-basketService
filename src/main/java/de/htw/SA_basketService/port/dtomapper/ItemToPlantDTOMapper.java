package de.htw.SA_basketService.port.dtomapper;

import de.htw.SA_basketService.port.dto.PlantChangeDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ItemToPlantDTOMapper {
    public PlantChangeDTO getPlantChangeDTO(UUID itemId, int difference){
        PlantChangeDTO plantChangeDTO = new PlantChangeDTO();
        plantChangeDTO.setPlantId(itemId);
        plantChangeDTO.setChangeAmount(difference);
        return plantChangeDTO;
    }
}
