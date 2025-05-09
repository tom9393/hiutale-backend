package com.hiutaleapp.util;

import com.hiutaleapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(reg -> {
                    // For now in order to help development
                    reg.requestMatchers("/users/register", "/users/login").permitAll();
                    reg.requestMatchers(HttpMethod.POST).hasRole("USER");
                    reg.requestMatchers(HttpMethod.DELETE).hasRole("USER");
                    reg.requestMatchers(HttpMethod.PUT).denyAll();
                    reg.requestMatchers(HttpMethod.PATCH).denyAll();
                    reg.requestMatchers(HttpMethod.GET).permitAll();
//                    reg.requestMatchers(
//                            "/users/register", "/users/login",
//                            "/events/all", "/events/one/**",
//                            "/locations/all", "/locations/one/**",
//                            "/reviews/all", "/reviews/one/**",
//                            "/categories/all", "/categories/one/**",
//                            "/event-categories/all", "/event-categories/one/**"
//
//                    ).permitAll();
//                    reg.requestMatchers(
//                            "/users/update/**", "/users/delete/**",
//                            "/events/create", "/events/delete/**",
//                            "/locations/create", "/locations/delete/**",
//                            "/favourites/create", "/favourites/delete/**",
//                            "/attendances/create", "/attendances/delete/**",
//                            "/reviews/create", "/reviews/delete/**",
//                            "/notifications/create/**", "/notifications/delete/**"
//                    ).hasRole("USER");
//                    reg.requestMatchers(("/**")).hasRole("ADMIN");
                    reg.anyRequest().authenticated();
        })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
