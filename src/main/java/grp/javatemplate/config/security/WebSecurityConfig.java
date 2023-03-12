package grp.javatemplate.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static grp.javatemplate.model.enums.UserRole.ROLE_ADMIN;
import static grp.javatemplate.model.enums.UserRole.ROLE_REGULAR;
import static org.springframework.http.HttpMethod.*;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Value("${endpoint.users}")
    String usersPath;
    private final JwtAuthConverter jwtAuthConverter;
    private static final String PATH_WILDCARD = "/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(GET, usersPath).authenticated()
                .requestMatchers(POST, usersPath).hasAnyRole(ROLE_REGULAR, ROLE_ADMIN)
                .requestMatchers(PUT, usersPath, usersPath + PATH_WILDCARD).hasAnyRole(ROLE_REGULAR, ROLE_ADMIN)
                .requestMatchers(DELETE, usersPath, usersPath + PATH_WILDCARD).hasRole(ROLE_ADMIN)
                .requestMatchers(GET, "/swagger-ui/**", "/v3/**").permitAll()
                .anyRequest().authenticated();
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}
