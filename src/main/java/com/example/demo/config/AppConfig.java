package com.example.demo.config;

import com.example.demo.enums.UserRole;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class AppConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {  // Only add users if none exist
                User user1 = new User("user1", passwordEncoder.encode("password1"), "user1@example.com", UserRole.ADMIN);
                userRepository.save(user1);
            }
        };
    }


}
