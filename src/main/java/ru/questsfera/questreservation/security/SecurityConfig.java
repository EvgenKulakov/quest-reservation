package ru.questsfera.questreservation.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> {
                    requests.requestMatchers("/login", "/logout", "/register/**",
                            "/css/**", "/js/**", "/images/**", "/error").permitAll();
                    requests.requestMatchers("/quests/**", "/accounts/**").hasAnyRole("ADMIN", "OWNER");
                    requests.anyRequest().authenticated();
                })
                .formLogin((form) ->  {
                    form.loginPage("/login");
                    form.defaultSuccessUrl("/");
                })
                .logout((logout) ->  {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login?logout");
                    logout.deleteCookies("JSESSIONID");
                    logout.invalidateHttpSession(true);
                });

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

