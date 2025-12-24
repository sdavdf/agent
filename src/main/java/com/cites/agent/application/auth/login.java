package com.cites.agent.application.auth;

public class login {

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(String token) {
    }

}
