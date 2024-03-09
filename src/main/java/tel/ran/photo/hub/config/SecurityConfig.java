package tel.ran.photo.hub.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tel.ran.photo.hub.service.PersonDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/auth/login", "/auth/registration", "/error", "/img/**").permitAll()
                                .anyRequest().hasAnyRole("USER", "ADMIN")
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/process_login")
                                .defaultSuccessUrl("/main", true)
                                .failureUrl("/auth/login?error")
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/auth/logout")
                                .logoutSuccessUrl("/auth/login")
                );
        return http.build();
    }
}
