package fr.thomascecil.heliodysse.adapter.in.dto.response.planet;


import fr.thomascecil.heliodysse.domain.model.enums.planetEnum.PlanetName;
import lombok.Data;

@Data
public class PlanetResponseDTO {
   private PlanetName planetName;
}
