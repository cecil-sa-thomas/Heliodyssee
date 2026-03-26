package fr.thomascecil.heliodysse.adapter.in.dto.request.spacePort;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpacePortCreateDTO {

    @NotBlank(message = "Name can't be blank")
    private String name;

    @NotNull(message = "Planet ID can't be null")
    private Short idPlanet;
}
