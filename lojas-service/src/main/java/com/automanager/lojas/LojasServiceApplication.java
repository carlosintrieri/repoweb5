package com.automanager.lojas;

import com.automanager.lojas.model.Loja;
import com.automanager.lojas.repository.LojaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LojasServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LojasServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(LojaRepository repo) {
        return args -> {
            Loja loja1 = new Loja();
            loja1.setNome("Toyota Moema SP");
            loja1.setCnpj("12.345.678/0001-00");
            loja1.setEndereco("Av. Moema, 1000");
            loja1.setCidade("São Paulo");
            loja1.setEstado("SP");
            loja1.setTelefone("(11) 3456-7890");
            loja1.setEmail("moema@toyota.com.br");

            Loja loja2 = new Loja();
            loja2.setNome("Volkswagen Campinas");
            loja2.setCnpj("98.765.432/0001-00");
            loja2.setEndereco("Rodovia Anhanguera, Km 95");
            loja2.setCidade("Campinas");
            loja2.setEstado("SP");
            loja2.setTelefone("(19) 3234-5678");
            loja2.setEmail("campinas@vw.com.br");

            repo.save(loja1);
            repo.save(loja2);

            System.out.println("\n=== LOJAS CRIADAS ===");
            System.out.println("Loja 1: Toyota Moema SP");
            System.out.println("Loja 2: Volkswagen Campinas\n");
        };
    }
}
