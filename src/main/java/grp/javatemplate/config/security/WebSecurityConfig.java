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

import static grp.javatemplate.model.enums.UserRole.REGULAR;
import static org.springframework.http.HttpMethod.*;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Value("${endpoint.users}")
    String usersPath;

//    String admin = ADMIN.toString();
    String admin = "app_admin";
    String regular = REGULAR.toString();

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(GET, "/users/user").hasAnyRole(regular, admin)
                .requestMatchers(GET, usersPath+"/admin").hasRole(admin)
                .requestMatchers(GET, "/swagger-ui/**", "/v3/**").permitAll()
                .requestMatchers(POST, usersPath).hasAnyRole(regular, admin)
                .requestMatchers(PUT, usersPath).hasAnyRole(regular, admin)
                .requestMatchers(DELETE, usersPath).hasRole(admin)
                .anyRequest().authenticated();
        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthConverter);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}
