package ru.questsfera.questreservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
                    logout.logoutSuccessUrl("/login?logout");
                    logout.deleteCookies("JSESSIONID");
                    logout.invalidateHttpSession(true);
                });

        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

