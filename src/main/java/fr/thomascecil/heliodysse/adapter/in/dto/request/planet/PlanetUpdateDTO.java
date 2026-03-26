package fr.thomascecil.heliodysse.adapter.in.dto.request.planet;

import fr.thomascecil.heliodysse.domain.model.enums.planetEnum.PlanetName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlanetUpdateDTO {
    @NotNull(message = "PlanetName can't be null")
    private PlanetName planetName;
}