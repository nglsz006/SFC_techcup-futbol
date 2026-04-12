package edu.dosw.project.SFC_TechUp_Futbol.config;

import edu.dosw.project.SFC_TechUp_Futbol.core.util.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    public SecurityConfig(JwtFilter jwtFilter, OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/access/register",
                    "/api/access/login",
                    "/api/access/oauth2/**",
                    "/api/admin/login",
                    "/oauth2/**",
                    "/login/oauth2/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers("/api/admin/users/**").hasRole("ADMINISTRADOR")
                .requestMatchers("/api/admin/audit/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/users/organizers/*/payments/*/approve").hasRole("ORGANIZADOR")
                .requestMatchers(HttpMethod.PUT, "/api/users/organizers/*/payments/*/reject").hasRole("ORGANIZADOR")
                .requestMatchers(HttpMethod.PUT, "/api/users/referees/*/matches/*/start").hasRole("ARBITRO")
                .requestMatchers(HttpMethod.PUT, "/api/users/referees/*/matches/*/result").hasRole("ARBITRO")
                .requestMatchers(HttpMethod.PUT, "/api/users/referees/*/matches/*/end").hasRole("ARBITRO")
                .requestMatchers(HttpMethod.POST, "/api/users/organizers/*/tournament").hasRole("ORGANIZADOR")
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2SuccessHandler)
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
