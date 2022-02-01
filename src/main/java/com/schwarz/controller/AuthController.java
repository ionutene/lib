package com.schwarz.controller;

import com.schwarz.domain.LoginRequestDTO;
import com.schwarz.domain.LoginResponseDTO;
import com.schwarz.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO loginResponseDTO=authService.login(loginRequest);

        return ResponseEntity.ok(loginResponseDTO);
    }

}
