package de.htw.SA_basketService.port.dtomapper;

import de.htw.SA_basketService.port.dto.PlantChangeDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ItemToPlantDTOMapper {
    public PlantChangeDTO getPlantChangeDTO(UUID plantId, int difference){
        PlantChangeDTO plantChangeDTO = new PlantChangeDTO();
        plantChangeDTO.setPlantId(plantId);
        plantChangeDTO.setChangeAmount(difference);
        return plantChangeDTO;
    }
}
