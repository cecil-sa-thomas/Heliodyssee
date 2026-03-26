package fr.thomascecil.heliodysse.adapter.in.security;

import fr.thomascecil.heliodysse.adapter.out.security.UserDetailsImpl;
import fr.thomascecil.heliodysse.domain.model.entity.User;
import fr.thomascecil.heliodysse.domain.port.in.UserUseCase;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserUseCase userUseCase;
    private final LoginAttemptService loginAttemptService;

    public CustomAuthenticationProvider(UserUseCase userUseCase, LoginAttemptService loginAttemptService) {
        this.userUseCase = userUseCase;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        //test contre brut force
        if (loginAttemptService.isBlocked(email)) {
            throw new LockedException("Trop de tentatives, compte temporairement bloqué.");
        }

        User user;
        try {
            user = userUseCase.checkLoginAndTriggerActivation(email, password);
        } catch (RuntimeException e) {
            loginAttemptService.loginFailed(email);
            throw new DisabledException(e.getMessage());
        }
        loginAttemptService.loginSucceeded(email); // reset si login OK

        UserDetailsImpl userDetails = new UserDetailsImpl(user); // Instancie UNE SEULE FOIS
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                password,
                userDetails.getAuthorities() //  Utiliser la même instance
        );
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

