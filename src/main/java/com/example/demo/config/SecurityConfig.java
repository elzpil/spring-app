package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${security.enable-csrf}")
    private boolean csrfEnabled;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/api/v1/**").permitAll()
				.anyRequest().permitAll()
                
			);
            if(!csrfEnabled)
            {
               http.csrf(AbstractHttpConfigurer::disable);
            }
		return http.build();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ?
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new IllegalStateException("User not found");
        };
    }
}
