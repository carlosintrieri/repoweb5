package com.automanager.funcionarios.controller;
import com.automanager.funcionarios.model.Funcionario;
import com.automanager.funcionarios.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lojas/{lojaId}/funcionarios")
public class FuncionarioController {
    @Autowired private FuncionarioService service;
    
    @GetMapping
    public ResponseEntity<List<Funcionario>> listar(@PathVariable Long lojaId) {
        List<Funcionario> lista = service.listarPorLoja(lojaId);
        lista.forEach(item -> {
            item.add(Link.of("/api/lojas/" + lojaId + "/funcionarios/" + item.getId(), "self"));
            item.add(Link.of("/api/lojas/" + lojaId, "loja"));
        });
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscar(@PathVariable Long lojaId, @PathVariable Long id) {
        Funcionario entity = service.buscarPorId(id);
        entity.add(Link.of("/api/lojas/" + lojaId + "/funcionarios/" + id, "self"));
        entity.add(Link.of("/api/lojas/" + lojaId, "loja"));
        return ResponseEntity.ok(entity);
    }
    
    @PostMapping
    public ResponseEntity<Funcionario> criar(@PathVariable Long lojaId, @RequestBody Funcionario entity) {
        Funcionario novo = service.criar(lojaId, entity);
        novo.add(Link.of("/api/lojas/" + lojaId + "/funcionarios/" + novo.getId(), "self"));
        return ResponseEntity.status(201).body(novo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable Long lojaId, @PathVariable Long id, @RequestBody Funcionario entity) {
        Funcionario atualizado = service.atualizar(lojaId, id, entity);
        atualizado.add(Link.of("/api/lojas/" + lojaId + "/funcionarios/" + id, "self"));
        return ResponseEntity.ok(atualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long lojaId, @PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> contar(@PathVariable Long lojaId) {
        return ResponseEntity.ok(service.contar(lojaId));
    }
    
    @GetMapping("/teste")
    public ResponseEntity<String> teste() {
        return ResponseEntity.ok("Funcionario Service OK");
    }
}
