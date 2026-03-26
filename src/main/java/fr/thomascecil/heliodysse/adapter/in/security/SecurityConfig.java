package fr.thomascecil.heliodysse.adapter.in.security;

import fr.thomascecil.heliodysse.adapter.out.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig {

    // 🔐 Fournisseur de service utilisateur
    @Bean
    public UserDetailsService userDetailsService(UserDetailsServiceImpl customService) {
        return customService;
    }

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/index", "/css/**", "/js/**", "/images/**",
                                "/users/register",
                                "/users/activate"  // 🔓 Ajoute cette ligne
                        ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/index")
                        .loginProcessingUrl("/connexion")
                        .successHandler(customAuthenticationSuccessHandler)  // 👈 Ici
                        .failureUrl("/index?error=true")
                        .permitAll()
                )


                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                )
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                        "default-src 'self'; " +
                                                "script-src 'self' https://js.stripe.com; " +
                                                "style-src 'self' https://fonts.googleapis.com https://m.stripe.network 'unsafe-inline'; " +
                                                "font-src 'self' https://fonts.googleapis.com https://fonts.gstatic.com; " +
                                                "connect-src 'self' https://api.stripe.com https://m.stripe.network; " +
                                                "frame-src 'self' https://js.stripe.com https://m.stripe.network https://hooks.stripe.com; " +
                                                "img-src 'self' https://m.stripe.network data:;"
                                )
                        )
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .build();
    }
}