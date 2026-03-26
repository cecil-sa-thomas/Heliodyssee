package fr.thomascecil.heliodysse.application.service;

import fr.thomascecil.heliodysse.domain.model.entity.User;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserRole;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserStatus;
import fr.thomascecil.heliodysse.domain.port.in.UserUseCase;
import fr.thomascecil.heliodysse.domain.port.out.UserRepository;
import fr.thomascecil.heliodysse.domain.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserApplicationService implements UserUseCase {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public UserApplicationService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = new UserService();

    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserEmail(String email) {
        userService.validateEmailFormat(email);
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserPhone(String phone) {
        userService.validatePhone(phone);
        return userRepository.findByPhone(phone);
    }

    @Override
    public List<User> getUserByLastName(String lastName) {
        return userRepository.findByLastName(lastName);
    }

    @Override
    public List<User> getUserByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName,lastName);
    }

    @Override
    public List<User> getUserByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> getUserStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }

    @Override
    public List<User> getUserByDateCreation(LocalDateTime date) {
        return userRepository.findByDateCreation(date);
    }

    @Override
    public List<User> getUserByDateCreationAfter(LocalDateTime date) {
        return userRepository.findByDateCreationAfter(date);
    }

    @Override
    public List<User> getUserByDateCreationBefore(LocalDateTime date) {
        return userRepository.findByDateCreationBefore(date);
    }

    @Override
    public List<User> getUserByDateCreationBetween(LocalDateTime start, LocalDateTime end) {
        return userRepository.findByDateCreationBetween(start, end);
    }

    //inscription
    @Override
    public User createUser(User user) {
        user.setStatus(UserStatus.INACTIVE);

        userService.validateUser(user);// Avant le hash du password pour verif du format

        //hash mot de passe
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(hashedPassword);
        userService.prepareActivation(user);
        User savedUser = userRepository.save(user);
        userService.sendActivationEmail(savedUser.getEmail(), savedUser.getActivationToken());
        return savedUser;
    }

    @Override
    public User checkLoginAndTriggerActivation(String email, String rawPassword) {
        User found = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email inconnu"));

        if (!passwordEncoder.matches(rawPassword, found.getPassword())) {
            System.out.println("Service : mot de passe incorrect"); // débogage
            throw new BadCredentialsException("Mot de passe incorrect");
        }

        if (found.getStatus() == UserStatus.INACTIVE) {
            userService.prepareActivation(found);
            userRepository.save(found); // on sauve le nouveau token
            userService.sendActivationEmail(found.getEmail(), found.getActivationToken());
            throw new RuntimeException("Compte inactif. Un mail d’activation a été envoyé.");
        }

        if (found.getStatus() == UserStatus.BANNED) {
            throw new RuntimeException("Compte banni.");
        }

        return found;
    }

    //USER
    @Override
    public User updateInfoUser(User updatingUser, String oldPassword) {
        User found = userRepository.findById(updatingUser.getIdUser())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        System.out.println("OldPassword  : " + oldPassword + " NewPassword  : " + found.getPassword() );
        if (!passwordEncoder.matches(oldPassword, found.getPassword())) {
            System.out.println("Service  : mot de passe incorrecte" );
            throw new BadCredentialsException("Mot de passe incorrect");
        }
        System.out.println("Service  : mot de passe correcte" );
        userService.updateUserFromUser(updatingUser, found);
        // Si un nouveau mot de passe est fourni, le hasher
        // Traite explicitement le mot de passe
        if (updatingUser.getPassword() != null && !updatingUser.getPassword().isBlank()) {
            String rawNewPassword = updatingUser.getPassword();

            if (!passwordEncoder.matches(rawNewPassword, found.getPassword())) {
                found.setPassword(passwordEncoder.encode(rawNewPassword));
            }
        }
        userService.updateInfoSystem(found);
        userService.validateUser(found);
        System.out.println("USER AVANT INSERTION : " + found);
        return userRepository.save(found);
    }

    //ADMIN
    @Override
    public User updateUser(User updatingUser) {
        User found = userRepository.findById(updatingUser.getIdUser())
                .orElseThrow(() -> new RuntimeException("User no found"));

        userService.updateUserFromUser(updatingUser, found);
        userService.updateInfoSystem(found);
        userService.validateUser(found);
        System.out.println("USER AVANT INSERTION : " + found);

        return userRepository.save(found);

    }


    @Override
    public void activateUserAccount(String token) {
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        if (user.getStatus() != UserStatus.INACTIVE)
            throw new RuntimeException("Le compte est déjà activé ou banni");

        if (user.getActivationTokenExpiration().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Le lien d’activation a expiré");

        user.setStatus(UserStatus.ACTIVE);
        user.setActivationToken(null);
        user.setActivationTokenExpiration(null);
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("No user found with id : " + id);
        }
        userRepository.deleteById(id);
    }
}
