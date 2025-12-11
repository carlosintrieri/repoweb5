package com.automanager.auth;

import com.automanager.auth.model.Usuario;
import com.automanager.auth.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
    
    @Bean
    public CommandLineRunner initData(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setSenha(encoder.encode("admin123"));
            admin.setPerfil("ADMIN");
            
            Usuario user = new Usuario();
            user.setUsername("user");
            user.setSenha(encoder.encode("user123"));
            user.setPerfil("USER");
            
            repo.save(admin);
            repo.save(user);
            
            System.out.println("\n=== USUÁRIOS CRIADOS ===");
            System.out.println("admin / admin123");
            System.out.println("user / user123\n");
        };
    }
}
