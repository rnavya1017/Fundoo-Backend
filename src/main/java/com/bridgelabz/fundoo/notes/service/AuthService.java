package com.bridgelabz.fundoo.notes.service;

import com.bridgelabz.fundoo.notes.dto.AuthResponseDTO;
import com.bridgelabz.fundoo.notes.dto.LoginRequestDTO;
import com.bridgelabz.fundoo.notes.dto.RegisterRequestDTO;
import com.bridgelabz.fundoo.notes.dto.ResetPasswordRequestDTO;

public interface AuthService {
    void register(RegisterRequestDTO requestDTO);
    AuthResponseDTO login(LoginRequestDTO requestDTO);
    String forgotPassword(String email);
    void resetPassword(ResetPasswordRequestDTO request);
}
