package tr.shadowise_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("userpass")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("adminpass")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/users/**", "/v3/api-docs/**", "/swagger-ui/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}