package fr.thomascecil.heliodysse.adapter.in.dto.request.spacePort;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpacePortUpdateDTO {
    @NotBlank(message = "Name can't be blank")
    private String name;
}
