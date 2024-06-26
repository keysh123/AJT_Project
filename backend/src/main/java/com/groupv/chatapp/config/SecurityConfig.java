package com.groupv.chatapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@Lazy
public class SecurityConfig {

    @Autowired
    private final JwtAuthFilter jwtAuthFilter;
    @Autowired
    private final CustomAuthProvider authenticationProvider;

    @Bean
    @Primary
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("SecurityConfig.securityFilterChain-----------------------------------------------------------------");
        http
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("**")
                )
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/v1/auth/**","/ws/**","**").permitAll() // Changed from requestMatchers to antMatchers
                        .anyRequest().authenticated()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("All good till now");
//        System.out.println(http.getObject());
        return http.build();
    }
}
