package com.IDOSdigital.userManagement.security;

import com.IDOSdigital.userManagement.security.token.JwtAuthFilter;
import com.IDOSdigital.userManagement.security.token.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final TokenProcessor tokenProcessor;

    @Autowired
    public SecurityConfiguration(TokenProcessor tokenProcessor) {
        this.tokenProcessor = tokenProcessor;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF if you're using stateless authentication (e.g., JWT)
                .authorizeHttpRequests(authorizeRequests ->
                {
                    try {
                        authorizeRequests
                                .requestMatchers("/graphql").permitAll() // Allow access to GraphQL endpoint
                                .anyRequest().authenticated() // Require authentication for other endpoints
                                .and()
                                .sessionManagement().disable()
                                .formLogin().disable();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                )
                .addFilterBefore(new JwtAuthFilter(tokenProcessor), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(List.of(authenticationProvider));
    }

}
