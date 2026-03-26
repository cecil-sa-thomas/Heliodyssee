package fr.thomascecil.heliodysse.adapter.in.controller.rest;

import fr.thomascecil.heliodysse.adapter.in.dto.mapper.UserDtoMapper;
import fr.thomascecil.heliodysse.adapter.in.dto.request.user.UserRequestDTO;
import fr.thomascecil.heliodysse.adapter.in.dto.response.user.UserResponseDTO;
import fr.thomascecil.heliodysse.adapter.out.security.UserDetailsImpl;
import fr.thomascecil.heliodysse.domain.model.entity.User;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserRole;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserStatus;
import fr.thomascecil.heliodysse.domain.port.in.UserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private final UserUseCase userUseCase;
    private final UserDtoMapper mapper;

    public UserRestController(UserUseCase userUseCase, UserDtoMapper mapper) {
        this.userUseCase = userUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userUseCase.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return userUseCase.getUserEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/phone")
    public ResponseEntity<User> getUserByPhone(@RequestParam String phone) {
        return userUseCase.getUserPhone(phone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lastname")
    public List<User> getByLastName(@RequestParam String lastName) {

        return userUseCase.getUserByLastName(lastName);
    }

    @GetMapping("/fullname")
    public List<User> getByFullName(@RequestParam String firstName, @RequestParam String lastName) {
        return userUseCase.getUserByFirstNameAndLastName(firstName, lastName);
    }

    @GetMapping("/role")
    public List<User> getByRole(@RequestParam String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            return userUseCase.getUserByRole(userRole);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role: " + role);
        }
    }

    @GetMapping("/status")
    public List<User> getByStatus(@RequestParam String status) {
        try {
            UserStatus userStatus = UserStatus.valueOf(status.toUpperCase());
            return userUseCase.getUserStatus(userStatus);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + status);
        }
    }

    @GetMapping("/created")
    public List<User> getByDateCreation(@RequestParam LocalDateTime date) {
        return userUseCase.getUserByDateCreation(date);
    }

    @GetMapping("/created-after")
    public List<User> getByDateCreationAfter(@RequestParam LocalDateTime date) {
        return userUseCase.getUserByDateCreationAfter(date);
    }

    @GetMapping("/created-before")
    public List<User> getByDateCreationBefore(@RequestParam LocalDateTime date) {
        return userUseCase.getUserByDateCreationBefore(date);
    }

    @GetMapping("/created-between")
    public List<User> getByDateCreationBetween(@RequestParam LocalDateTime start,
                                               @RequestParam LocalDateTime end) {
        return userUseCase.getUserByDateCreationBetween(start, end);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userUseCase.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    //INSCRIPTION
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        System.out.println("DTO reçu dans le controller : " + userRequestDTO);
        User user = mapper.toDomain(userRequestDTO);
        User createdUser = userUseCase.createUser(user);
        UserResponseDTO responseDTO = mapper.toDto(createdUser);
        return ResponseEntity.ok(responseDTO);
    }

    //Activation du compte
    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String token) {
        System.out.println("⚡ TOKEN REÇU : " + token);
        userUseCase.activateUserAccount(token); // ou un update partiel sans oldPassword
        return ResponseEntity.ok("✅ Compte activé ! Vous pouvez vous connecter.");
    }

    //ROUTE USER AUTH :
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getIdUser();
        return userUseCase.getUserById(userId)
                .map(user -> ResponseEntity.ok(mapper.toDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/test-me")
    public ResponseEntity<UserResponseDTO> updateCurrentUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserRequestDTO dto) {
        User user = mapper.toDomain(dto);
        user.setIdUser(userDetails.getUser().getIdUser());//on attribue l'id de l'utilisateur connecté
        String oldPassword = dto.getOldPassword();
        User updatedUser = userUseCase.updateInfoUser(user, oldPassword);
        UserResponseDTO responseDTO = mapper.toDto(updatedUser);

        return ResponseEntity.ok(responseDTO);
    }



    /*
    Pour éviter la redondance des route
    @GetMapping
public ResponseEntity<List<UserResponseDTO>> searchUsers(
        @RequestParam Optional<String> mail,
        @RequestParam Optional<String> phone,
        @RequestParam Optional<String> firstName,
        @RequestParam Optional<String> lastName,
        @RequestParam Optional<UserStatus> status,
        @RequestParam Optional<UserRole> role,
        @RequestParam Optional<LocalDateTime> createdAfter,
        @RequestParam Optional<LocalDateTime> createdBefore,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String order
)
     */
}