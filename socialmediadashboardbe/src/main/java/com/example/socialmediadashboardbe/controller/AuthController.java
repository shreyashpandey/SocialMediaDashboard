package com.example.socialmediadashboardbe.controller;


import com.example.socialmediadashboardbe.dto.AuthRequest;
import com.example.socialmediadashboardbe.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody AuthRequest authRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        // Generate JWT token
        String token = jwtUtil.generateToken(authRequest.getUsername());

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("username", authRequest.getUsername());
        response.put("token", token);

        return response;
    }
}
