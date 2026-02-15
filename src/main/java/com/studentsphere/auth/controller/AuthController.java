package com.studentsphere.auth.controller;

import com.studentsphere.auth.dto.AuthResponse;
import com.studentsphere.auth.dto.SignInRequest;
import com.studentsphere.auth.dto.SignUpRequest;
import com.studentsphere.auth.dto.UserResponse;
import com.studentsphere.auth.entity.User;
import com.studentsphere.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            User user = authService.signUp(request);
            UserResponse resp = authService.toUserResponse(user);
            return ResponseEntity.status(201).body(resp);
        } catch (RuntimeException e) {
            Map<String,String> err = Collections.singletonMap("message", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            AuthResponse auth = authService.signIn(request);
            return ResponseEntity.ok(auth);
        } catch (RuntimeException e) {
            Map<String,String> err = Collections.singletonMap("message", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    // Update profile — minimal fields (fullName, profilePictureUrl)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody UserResponse update) {
        try {
            User updated = authService.updateUserProfile(id, update);
            return ResponseEntity.ok(authService.toUserResponse(updated));
        } catch (RuntimeException e) {
            Map<String,String> err = Collections.singletonMap("message", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }
}
