package com.bridgelabz.fundoo.notes.service.impl;

import com.bridgelabz.fundoo.notes.dto.AuthResponseDTO;
import com.bridgelabz.fundoo.notes.dto.AuthenticatedUserDTO;
import com.bridgelabz.fundoo.notes.dto.LoginRequestDTO;
import com.bridgelabz.fundoo.notes.dto.RegisterRequestDTO;
import com.bridgelabz.fundoo.notes.dto.ResetPasswordRequestDTO;
import com.bridgelabz.fundoo.notes.entity.User;
import com.bridgelabz.fundoo.notes.exception.DuplicateEmailException;
import com.bridgelabz.fundoo.notes.exception.InvalidPasswordException;
import com.bridgelabz.fundoo.notes.exception.InvalidTokenException;
import com.bridgelabz.fundoo.notes.exception.UserNotFoundException;
import com.bridgelabz.fundoo.notes.redis.TokenCacheService;
import com.bridgelabz.fundoo.notes.repository.UserRepository;
import com.bridgelabz.fundoo.notes.security.JwtService;
import com.bridgelabz.fundoo.notes.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenCacheService tokenCacheService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, TokenCacheService tokenCacheService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenCacheService = tokenCacheService;
    }

    @Override
    public void register(RegisterRequestDTO requestDTO){
        String email = requestDTO.getEmail().trim().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException("Email already exists");
        }

        User user = new User();

        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setMobile(requestDTO.getMobile());
        user.setCreatedDate(LocalDateTime.now());
        user.setUpdatedDate(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO requestDTO) {
        String email = requestDTO.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!passwordEncoder.matches(
                requestDTO.getPassword(),
                user.getPassword()
        )){
            throw new InvalidPasswordException("Invalid password");
        }
        String token = jwtService.generateToken(user.getEmail());

        long expirationMillis = jwtService.getExpirationTime();

        tokenCacheService.saveToken(
                token,
                user.getEmail(),
                expirationMillis
        );

        log.info("User login successful: {}", user.getEmail());

        return new AuthResponseDTO(
                token,
                new AuthenticatedUserDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()
                )
        );
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String resetToken = UUID.randomUUID().toString();

        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        return resetToken;
    }
    @Override
    public void resetPassword(ResetPasswordRequestDTO request) {
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getResetToken() == null ||
                !user.getResetToken().equals(request.getResetToken())) {

            throw new InvalidTokenException("Invalid reset token");
        }

        if (user.getResetTokenExpiry() == null ||
                user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {

            throw new InvalidTokenException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }
}
