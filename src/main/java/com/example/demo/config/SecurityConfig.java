package com.example.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.config.auth.SecurityFilter;
import com.example.demo.user.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${security.enable-csrf}")
    private boolean csrfEnabled;

    @Autowired
    SecurityFilter securityFilter;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        
		http
        
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/*").permitAll()
            //.requestMatchers(HttpMethod.POST, "/api/v1/student/*").hasRole("ADMIN")
            .anyRequest().permitAll());
            //.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
            
            if(!csrfEnabled)
            {
            http.csrf(AbstractHttpConfigurer::disable);
            } 
            
		return http.build();
	}

    /* @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } */

    // ?
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new IllegalStateException("User not found userDetailsService()");
        };
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userService);  
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> myAuthenticationProviders) {
        return new ProviderManager(myAuthenticationProviders);
    }
    /* @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        
        return authenticationConfiguration.getAuthenticationManager();
    } */
}


