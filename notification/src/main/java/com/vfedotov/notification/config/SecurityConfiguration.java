package com.vfedotov.notification.config;

import com.vfedotov.notification.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private UserService userService;

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(this.userService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
         .authorizeHttpRequests(auth -> auth
                 .requestMatchers("/main_page", "/registration_page", "/error_page").permitAll()
                 .requestMatchers("/users/register").permitAll()
                 .requestMatchers("/css/**", "/images/**").permitAll()
                 .requestMatchers("/swagger-ui/**", "/v3/**").hasAuthority("ADMIN")
                 .anyRequest().authenticated())
         .formLogin(login -> login
                 .loginPage("/login_page")
                 .usernameParameter("login")
                 .passwordParameter("password")
                 .loginProcessingUrl("/login")
                 .defaultSuccessUrl("/main_page")
                 .failureUrl("/login_page?error=true")
                 .permitAll())
         .logout(logout -> logout
                 .logoutUrl("/logout")
                 .logoutSuccessUrl("/main_page")
                 .deleteCookies("JSESSIONID")
                 .permitAll());
         return http.build();
    }
}
