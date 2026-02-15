package com.studentsphere.auth.service;

import com.studentsphere.auth.dto.AuthResponse;
import com.studentsphere.auth.dto.SignInRequest;
import com.studentsphere.auth.dto.SignUpRequest;
import com.studentsphere.auth.dto.UserResponse;
import com.studentsphere.auth.entity.User;
import com.studentsphere.auth.repository.UserRepository;
import com.studentsphere.master.entity.College;
import com.studentsphere.master.repository.CollegeRepository;
import com.studentsphere.common.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256; // bits
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private static String hashPasswordPBKDF2(char[] password, byte[] salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }

    @Transactional
    public User signUp(SignUpRequest request) {
        // 1. Check password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        // 2. Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // 3. Verify college exists
        College college = collegeRepository.findById(request.getCollegeId())
                .orElseThrow(() -> new RuntimeException("College not found"));

        // 4. Ensure student email matches college domain
        if (!request.getStudentCollegeEmail().endsWith(college.getDomain())) {
            throw new RuntimeException("Student email does not match college domain: " + college.getDomain());
        }

        // 5. Check if student email is already used
        if (userRepository.existsByStudentCollegeEmail(request.getStudentCollegeEmail())) {
            throw new RuntimeException("Student college email already in use");
        }

        byte[] salt = generateSalt();
        String hash = hashPasswordPBKDF2(request.getPassword().toCharArray(), salt, ITERATIONS);
        String stored = ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" + hash;

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(stored); // store iterations:salt:hash
        user.setStudentCollegeEmail(request.getStudentCollegeEmail());
        user.setCollege(college);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse signIn(SignInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String stored = user.getPassword();
        boolean matches = false;

        if (stored != null && stored.contains(":")) {
            String[] parts = stored.split(":");
            if (parts.length == 3) {
                int iterations = Integer.parseInt(parts[0]);
                byte[] salt = Base64.getDecoder().decode(parts[1]);
                String storedHash = parts[2];

                String incomingHash = hashPasswordPBKDF2(request.getPassword().toCharArray(), salt, iterations);
                matches = incomingHash.equals(storedHash);
            }
        } else {
            throw new RuntimeException("Invalid stored password format");
        }

        if (!matches) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT tokens
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getFullName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        String collegeName = user.getCollege() != null ? user.getCollege().getName() : null;
        return new AuthResponse(accessToken, refreshToken, user.getId(), user.getFullName(), collegeName);
    }

    public UserResponse toUserResponse(User user) {
        if (user == null) return null;
        Long collegeId = user.getCollege() != null ? user.getCollege().getId() : null;
        String collegeName = user.getCollege() != null ? user.getCollege().getName() : null;
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getStudentCollegeEmail(), collegeId, collegeName, user.getProfilePictureUrl());
    }

    @Transactional
    public User updateUserProfile(Long userId, UserResponse update) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (update.getFullName() != null && !update.getFullName().trim().isEmpty()) {
            user.setFullName(update.getFullName());
        }
        if (update.getProfilePictureUrl() != null) {
            user.setProfilePictureUrl(update.getProfilePictureUrl());
        }
        // Note: we don't allow email or studentCollegeEmail changes here to keep identity stable
        return userRepository.save(user);
    }
}
