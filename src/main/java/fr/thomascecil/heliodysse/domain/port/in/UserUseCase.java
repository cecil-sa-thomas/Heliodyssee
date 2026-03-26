package fr.thomascecil.heliodysse.domain.port.in;

import fr.thomascecil.heliodysse.domain.model.entity.User;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserRole;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserUseCase {
    Optional<User> getUserById(Long id);
    List<User> getAll();
    Optional<User> getUserEmail(String email);
    Optional<User> getUserPhone(String phone);
    List<User> getUserByLastName(String lastName);
    List<User> getUserByFirstNameAndLastName(String firstName,String lastName);
    List<User> getUserByRole(UserRole role);
    List<User> getUserStatus(UserStatus status);
    List<User> getUserByDateCreation(LocalDateTime date);
    List<User> getUserByDateCreationAfter(LocalDateTime date);
    List<User> getUserByDateCreationBefore(LocalDateTime date);
    List<User> getUserByDateCreationBetween(LocalDateTime start, LocalDateTime end);
    User createUser(User user);
    User checkLoginAndTriggerActivation(String email, String password);
    User updateInfoUser(User user, String oldPassword);
    User updateUser(User user);

    void activateUserAccount(String token);
    void deleteUserById(Long id);
}
