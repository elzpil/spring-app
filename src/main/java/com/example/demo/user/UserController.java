package com.example.demo.user;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.config.auth.TokenProvider;
import com.example.demo.dtos.JwtDto;
import com.example.demo.dtos.SignInDto;
import com.example.demo.dtos.SignUpDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body(null);
        }
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody  SignUpDto data) {
        System.out.println("signing up ------------------------" + data.login());
        userService.signUp(data);
        System.out.println("signed up ------------------------");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInDto data) {
        try {
            
            System.out.println("Attempting authentication for user: " + data.login()); 
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

            System.out.println("Attempting authUser: " + usernamePassword.getName());
            // does not authenticate right username and password
            var authUser = authenticationManager.authenticate(usernamePassword);

            System.out.println("Attempting generate token: " + data.login());
            var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());

            System.out.println("User authenticated successfully: " + data.login());

            return ResponseEntity.ok(new JwtDto(accessToken));
        } catch (Error e) {
            
            System.out.println("Authentication failed for user: " + data.login());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
