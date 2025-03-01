package com.incidentmanagement.controller;

import com.incidentmanagement.dto.LoginRequestDTO;
import com.incidentmanagement.dto.LoginResponseDTO;
import com.incidentmanagement.dto.UserDTO;
import com.incidentmanagement.dto.UserRegistrationDTO;
import com.incidentmanagement.model.User;
import com.incidentmanagement.repository.UserRepository;
import com.incidentmanagement.security.JwtTokenProvider;
import com.incidentmanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                          UserRepository userRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            User user = userService.registerUser(registrationDTO);
            UserDTO userDTO = userService.convertToDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtTokenProvider.createToken(user.getEmail(), user.getId());
            UserDTO userDTO = userService.convertToDTO(user);

            return ResponseEntity.ok(new LoginResponseDTO(token, userDTO));
        } catch (AuthenticationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid email or password");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Email is required");
            return ResponseEntity.badRequest().body(response);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "If your email is registered, you will receive password reset instructions");
        return ResponseEntity.ok(response);
    }
}