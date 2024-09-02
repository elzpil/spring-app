package com.example.demo.config.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenProvider tokenService;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
            if ((request.getRequestURI().contains("/api/v1/auth/signup")|| request.getRequestURI().contains("/api/v1/auth/signin")) && !hasToken(request)) {
                // If it's a signup request and doesn't have a token, skip authentication logic
                System.out.println("==============filterChain.doFilter(request, response);=================");
                filterChain.doFilter(request, response);
                return;
            }
        var token = this.recoverToken(request);
        if (token != null) {
            var login = tokenService.validateToken(token);
            var optionalUser = userRepository.findByUsername(login);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                filterChain.doFilter(request, response);
        } else {
            throw new RuntimeException("User not found doFilterInternal()");
        }
        
    }

    private boolean hasToken(HttpServletRequest request) {
        // Example: Check if token exists in request header or parameter
        String token = request.getHeader("Authorization");
        return token != null && token.startsWith("Bearer ");
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
        return null;
        return authHeader.replace("Bearer ", "");
    }
}
