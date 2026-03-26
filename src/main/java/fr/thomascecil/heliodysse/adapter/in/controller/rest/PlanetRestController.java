package fr.thomascecil.heliodysse.adapter.in.controller.rest;

import fr.thomascecil.heliodysse.adapter.in.dto.mapper.PlanetDtoMapper;
import fr.thomascecil.heliodysse.adapter.in.dto.request.planet.PlanetCreateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.request.planet.PlanetUpdateDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.planet.PlanetResponseDTO;
import fr.thomascecil.heliodysse.domain.model.entity.Planet;
import fr.thomascecil.heliodysse.domain.port.in.PlanetUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/planets")
public class PlanetRestController {
    private final PlanetUseCase planetUseCase;
    private final PlanetDtoMapper mapper;

    public PlanetRestController(PlanetUseCase planetUseCase, PlanetDtoMapper mapper) {
        this.planetUseCase = planetUseCase;
        this.mapper = mapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<Planet> getPlanetById(@PathVariable Short id){
        return planetUseCase.getPlanetById(id)
                .map((planet -> ResponseEntity.ok(planet)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<PlanetResponseDTO> getAllPlanet(){
        List<Planet> planets = planetUseCase.getAll();
        List<PlanetResponseDTO> planetResponseDTO = planets.stream().map(mapper::toDto).collect(Collectors.toList());
        return planetResponseDTO;
    };

    @PostMapping
    public ResponseEntity<PlanetResponseDTO> createPlanet(@RequestBody PlanetCreateDTO planetCreateDTO){
        Planet planet = mapper.toDomain(planetCreateDTO);
        Planet createdPlanet = planetUseCase.createPlanet(planet);
        PlanetResponseDTO planetResponseDTO = mapper.toDto(createdPlanet);
        return ResponseEntity.ok(planetResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanetResponseDTO> updatePlanet(@PathVariable Short id, @RequestBody PlanetUpdateDTO planetUpdateDTO) {
        // Récupération et mise à jour - TODO: refactoriser vers le use case
        System.out.println("Json brut : " + planetUpdateDTO);
        Planet planet = planetUseCase.getPlanetById(id)
                .orElseThrow(() -> new EntityNotFoundException("Planet not found"));
        System.out.println("DTO name: " + planetUpdateDTO.getPlanetName());
        mapper.updatePlanetFromDto(planetUpdateDTO, planet);
        planetUseCase.updatePlanet(planet);
        System.out.println("PLANET name: " + planet.getPlanetName());
        PlanetResponseDTO updatedDto = mapper.toDto(planet);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanet(@PathVariable Short id) {
        planetUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
