package fr.thomascecil.heliodysse.domain.service;

import fr.thomascecil.heliodysse.domain.model.entity.User;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserRole;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;


public class UserService {

    public void validateUser(User user) {
        checkRequiredFields(user);
        validateEmailFormat(user.getEmail());
        validatePassword(user.getPassword());
        validatePhone(user.getPhone());
        isAdult(user);
        initalizedDefaultsSelf(user);
        validateRole(user);
        validateStatus(user);
    }

    public void checkRequiredFields(User user) {
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name mandatory.");
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name mandatory.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email mandatory.");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password mandatory.");
        }
        if (user.getPhone() == null || user.getPhone().isBlank()) {
            throw new IllegalArgumentException("Phone number mandatory.");
        }
        if (user.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Birthdate mandatory.");
        }
    }

    public void validateEmailFormat(String email) {
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email not valid");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide.");
        }

        if (password.length() < 12) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 12 caractères.");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins une majuscule.");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins une minuscule.");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins un chiffre.");
        }

        if (!password.matches(".*[@$!%*?&].*")) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins un caractère spécial (@$!%*?&).");
        }
    }


    public void validatePhone(String phone) {
        if (!phone.matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException("Téléphone invalide.");
        }
    }

    public void isAdult(User user) {
        if (user.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Birthdate mandatory.");
        }
        int age = Period.between(user.getDateOfBirth(), LocalDate.now()).getYears();
        if (age < 18) {
            throw new IllegalArgumentException("User must be at least 18 years old.");
        }
    }

    /**
     * Met à jour les champs non null de l'objet source dans l'objet cible.
     *
     * Cette méthode applique une logique de "patch partiel" :
     * seuls les champs explicitement renseignés (non null) dans l'objet `source`
     * seront copiés vers l'objet `target`. Cela permet :
     *
     * - d'éviter d'écraser des données existantes avec des valeurs null par erreur ;
     * - de gérer des formulaires partiels (où seul un sous-ensemble des champs est modifié) ;
     * - d'utiliser la même méthode pour des mises à jour progressives ou ciblées.
     *
     * Attention : si une propriété est volontairement mise à null pour être effacée,
     * cette méthode ne le prendra pas en compte. Dans ce cas, une logique différente
     * (par exemple via DTO ou wrapper dédié) sera nécessaire.
     */
    public void updateUserFromUser(User source, User target) {
//        if (source.getPassword() != null && !source.getPassword().isBlank()) {  //hashage et écrasement directement dans le service
//            target.setPassword(source.getPassword());
//        }
        if (source.getLastName() != null && !source.getLastName().isBlank()) {
            target.setLastName(source.getLastName());
        }
        if (source.getFirstName() != null && !source.getFirstName().isBlank()) {
            target.setFirstName(source.getFirstName());
        }
        if (source.getEmail() != null && !source.getEmail().isBlank()) {
            target.setEmail(source.getEmail());
        }
        if (source.getPhone() != null && !source.getPhone().isBlank()) {
            target.setPhone(source.getPhone());
        }
        if (source.getStripeCustomerId() != null && !source.getStripeCustomerId().isBlank()) {
            target.setStripeCustomerId(source.getStripeCustomerId());
        }
        if (source.getActivationToken() != null && !source.getActivationToken().isBlank()) {
            target.setActivationToken(source.getActivationToken());
        }

        // Champs non-String
        if (source.getStatus() != null) target.setStatus(source.getStatus());
        if (source.getRole() != null) target.setRole(source.getRole());
        if (source.getDateOfBirth() != null) target.setDateOfBirth(source.getDateOfBirth());
        if (source.getActivationTokenExpiration() != null) target.setActivationTokenExpiration(source.getActivationTokenExpiration());
        if (source.getLastModificationDate() != null) target.setLastModificationDate(source.getLastModificationDate());
        if (source.getLastModificationBy() != null && !source.getLastModificationBy().isBlank()) {
            target.setLastModificationBy(source.getLastModificationBy());
        }
        if (source.getVersion() != null) target.setVersion(source.getVersion());
    }

    public void initalizedDefaultsSelf(User user){
        if (user.getRole()== null){
            user.setRole(UserRole.CLIENT);
        }
        if (user.getStatus()== null){
            user.setStatus(UserStatus.INACTIVE);
        }
        if (user.getCreatedBy() == null) {
            user.setCreatedBy("placeHolder");
        }
        if (user.getVersion() == null) {
            user.setVersion((short) 0);
        }
        if (user.getDateCreation() == null) {
            user.setDateCreation(LocalDateTime.now());
        }
    }

    //Utile pour admin -> create, update
    public void validateRole(User user){
        if(user.getRole() == null){
            throw new IllegalArgumentException("Role not found");
        }
        boolean isValid = switch (user.getRole()){
            case ADMIN, CLIENT -> true;
            default -> false;
        };
        if(!isValid){
            throw new IllegalArgumentException("Role non authorized : " + user.getRole());
        }
    }

    //Utile pour admin -> create, update
    public void validateStatus(User user){
        if(user.getStatus() == null){
            throw new IllegalArgumentException("Status not found");
        }
        boolean isValid = switch (user.getStatus()){
            case ACTIVE, INACTIVE, BANNED -> true;
            default -> false;
        };
        if(!isValid){
            throw new IllegalArgumentException("Status non authorized : " + user.getStatus());
        }
    }
    // "system" pour le moment -> info user par la suite ?
    public void updateInfoSystem(User user){
        user.setLastModificationDate(LocalDateTime.now());
        user.setLastModificationBy("system");
    }

    public void prepareActivation(User user) {
        String token = UUID.randomUUID().toString();
        user.setActivationToken(token);
        user.setActivationTokenExpiration(LocalDateTime.now().plusHours(24));
    }

    //En attendant d'ajouter une library qui gère l'envoi de mail
    public void sendActivationEmail(String email, String token) {
        // Simule un envoi de mail en console pour l’instant
        String link = "http://localhost:8080/users/activate?token=" + token;
        System.out.println("✉️ Mail envoyé à : " + email);
        System.out.println("👉 Lien d'activation : " + link);
    }
}
