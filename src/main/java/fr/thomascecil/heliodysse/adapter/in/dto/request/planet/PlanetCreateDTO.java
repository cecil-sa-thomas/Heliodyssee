package fr.thomascecil.heliodysse.adapter.in.dto.request.planet;

import fr.thomascecil.heliodysse.domain.model.enums.planetEnum.PlanetName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlanetCreateDTO {
    @NotNull(message = "Name Can't be blank")
    PlanetName planetName;
}
