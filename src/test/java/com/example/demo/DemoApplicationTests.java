package com.example.demo;

import com.example.demo.student.Student;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /* @Test
    public void givenInvalidStudent_whenRegisterNewStudent_thenValidationErrors() throws Exception {
        // Create an invalid student
        Student invalidStudent = new Student("Invalid Name", "invalid_email", LocalDate.of(2000, 2, 1));
        String jsonStudent = objectMapper.writeValueAsString(invalidStudent);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStudent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting a 400 Bad Request
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email should be valid")); // Adjusted to match the actual structure
    } */

}
