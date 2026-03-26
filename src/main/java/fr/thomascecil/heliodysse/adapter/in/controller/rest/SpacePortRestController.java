package fr.thomascecil.heliodysse.adapter.in.controller.rest;

import fr.thomascecil.heliodysse.adapter.in.dto.mapper.SpacePortDtoMapper;
import fr.thomascecil.heliodysse.adapter.in.dto.request.spacePort.SpacePortCreateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.request.spacePort.SpacePortUpdateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.spacePort.SpacePortResponseDTO;
import fr.thomascecil.heliodysse.domain.model.entity.SpacePort;
import fr.thomascecil.heliodysse.domain.port.in.SpacePortUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spaceports")
public class SpacePortRestController {
    private final SpacePortUseCase spacePortUseCase;
    private final SpacePortDtoMapper mapper;

    public SpacePortRestController(SpacePortUseCase spacePortUseCase, SpacePortDtoMapper mapper) {
        this.spacePortUseCase = spacePortUseCase;
        this.mapper = mapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<SpacePortResponseDTO> getSpacePortById(@PathVariable Integer id) {
        return spacePortUseCase.getSpacePortById(id)
                .map(spacePort -> ResponseEntity.ok(mapper.toDto(spacePort)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<SpacePortResponseDTO> getAllSpacePorts() {
        List<SpacePort> spacePorts = spacePortUseCase.getAll();
        return spacePorts.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/planets/{id}")
    public List<SpacePortResponseDTO> getSpacePortsByPlanets(@PathVariable Short id) {
        List<SpacePort> spacePorts = spacePortUseCase.getByPlanets(id);
        return spacePorts.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<SpacePortResponseDTO> createSpacePort(@RequestBody SpacePortCreateDTO spacePortCreateDTO) {
        SpacePort spacePort = mapper.toDomain(spacePortCreateDTO);
        SpacePort createdSpacePort = spacePortUseCase.createSpacePort(spacePort);
        SpacePortResponseDTO responseDTO = mapper.toDto(createdSpacePort);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpacePortResponseDTO> updateSpacePort(@PathVariable Integer id, @RequestBody SpacePortUpdateDTO spacePortUpdateDTO) {
        // Récupération et mise à jour - TODO: refactoriser vers le use case
        SpacePort spacePort = spacePortUseCase.getSpacePortById(id)
                .orElseThrow(() -> new EntityNotFoundException("SpacePort not found"));

        mapper.updateSpacePortFromDto(spacePortUpdateDTO, spacePort);
        SpacePort updated = spacePortUseCase.updateSpacePort(spacePort);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpacePort(@PathVariable Integer id) {
        spacePortUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

