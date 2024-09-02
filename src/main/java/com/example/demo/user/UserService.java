package com.example.demo.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.SignUpDto;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername() in UserService " );
        var optionalUser = userRepository.findByUsername(username);
        System.out.println(optionalUser );
        if (optionalUser.isPresent()) {
            User user = optionalUser.get(); 
            return user;
        } else {
            throw new RuntimeException("User not found loadUserByUsername()");
        }
    }

    public User registerUser(User user) {  
        user.setPassword(passwordEncoder.encode(user.getPassword()));    
        return userRepository.save(user);
    }

    public UserDetails signUpd(SignUpDto data)  {
        if (userRepository.findByUsername(data.login()) != null) {
            throw new Error("Username already exists");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.email(), data.role());
        return userRepository.save(newUser);
    }

    @Transactional 
    public UserDetails signUp(SignUpDto data)  {
    
        Optional<User> existingUserOptional = userRepository.findByUsername(data.login());
        if (existingUserOptional.isPresent()) {
            throw new RuntimeException("Username already exists");
        }
    
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, data.email(), data.role());
        User savedUser = userRepository.save(newUser);
    
    return savedUser; 
}

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }


}