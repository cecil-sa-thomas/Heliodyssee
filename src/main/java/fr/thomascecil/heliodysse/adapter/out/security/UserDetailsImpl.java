package fr.thomascecil.heliodysse.adapter.out.security;

import fr.thomascecil.heliodysse.domain.model.entity.User;

import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserRole;
import fr.thomascecil.heliodysse.domain.model.enums.userEnum.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = Objects.requireNonNull(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // On mappe le rôle métier en rôle Spring Security
        UserRole role = user.getRole();
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // haché avec BCrypt
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // identifiant de connexion
    }

    @Override
    public boolean isAccountNonExpired() {
        //Todo Ajoutez un logique d'expiation de compte si pas actif depuis 2 ans par exemple
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getStatus().equals(UserStatus.BANNED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //Todo Ajoutez un logique d'expiation de mdp si necessaire
        return true;
    }

    @Override
    public boolean isEnabled() {
        //Todo voir la logique derrière une première connexion
        return user.getStatus().equals(UserStatus.ACTIVE);
    }

    public User getUser() {
        return this.user;
    }
}
