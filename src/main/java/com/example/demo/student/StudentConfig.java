package com.example.demo.student;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner (StudentRepository repository){
        return args -> {
            Student name = new Student (1L, "name", "email@email.com", LocalDate.of(2000, Month.MAY, 5));
            Student name2 = new Student ("name2", "email2@email.com", LocalDate.of(2000, Month.MAY, 6));
            repository.saveAll(List.of(name,  name2));
        };
    }
}
