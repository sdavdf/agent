package com.cites.agent.application.auth;

import com.cites.agent.application.auth.login.LoginRequest;
import com.cites.agent.application.auth.login.LoginResponse;
import com.cites.agent.infrastructure.security.JwtService;
import org.springframework.security.core.Authentication;
import com.cites.agent.infrastructure.security.UserPrincipal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
                )
            );

        UserPrincipal principal =
            (UserPrincipal) authentication.getPrincipal();

        String token = jwtService.generateToken(principal);

        return new LoginResponse(token);
    }
}
