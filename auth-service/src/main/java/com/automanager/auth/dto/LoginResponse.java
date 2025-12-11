package com.automanager.auth.dto;

public class LoginResponse {
    private String token;
    private String username;
    private String perfil;
    
    public LoginResponse(String token, String username, String perfil) {
        this.token = token;
        this.username = username;
        this.perfil = perfil;
    }
    
    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getPerfil() { return perfil; }
}
