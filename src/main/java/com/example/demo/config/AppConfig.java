package com.example.demo.config;

import com.example.demo.enums.UserRole;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
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
    CommandLineRunner commandLineRunner(UserRepository userRepository, StudentRepository studentRepository) {
        return args -> {
            User user1 = new User("user1", passwordEncoder.encode("password1"), "user1@example.com", UserRole.ADMIN);
            userRepository.saveAll(List.of(user1));

            Student name = new Student (1L, "name", "email@email.com", LocalDate.of(2000, Month.MAY, 5));
            Student name2 = new Student ("name2", "email2@email.com", LocalDate.of(2000, Month.MAY, 6));
            studentRepository.saveAll(List.of(name,  name2));
        };
    }


}
