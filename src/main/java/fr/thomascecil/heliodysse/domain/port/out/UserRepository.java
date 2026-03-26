package fr.thomascecil.heliodysse.domain.port.out;

import fr.thomascecil.heliodysse.domain.model.entity.User;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserRole;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    List<User> findAll();
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    List<User> findByLastName(String lastName);
    List<User> findByFirstNameAndLastName(String firstName,String lastName);
    List<User> findByRole(UserRole role);
    List<User> findByStatus(UserStatus status);
    List<User> findByDateCreation(LocalDateTime date);
    List<User> findByDateCreationAfter(LocalDateTime date);
    List<User> findByDateCreationBefore(LocalDateTime date);
    List<User> findByDateCreationBetween(LocalDateTime start, LocalDateTime end);

    User save(User user);
    Optional<User> findByActivationToken(String token);

    void deleteById(Long id) throws IllegalArgumentException;

}