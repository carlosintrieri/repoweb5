package com.automanager.auth.service;

import com.automanager.auth.dto.*;
import com.automanager.auth.model.Usuario;
import com.automanager.auth.repository.UsuarioRepository;
import com.automanager.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository repository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder encoder;
    
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if (!encoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }
        
        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getPerfil());
        return new LoginResponse(token, usuario.getUsername(), usuario.getPerfil());
    }
}
