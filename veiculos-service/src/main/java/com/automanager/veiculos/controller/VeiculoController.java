package com.automanager.veiculos.controller;
import com.automanager.veiculos.model.Veiculo;
import com.automanager.veiculos.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lojas/{lojaId}/veiculos")
public class VeiculoController {
    @Autowired private VeiculoService service;
    
    @GetMapping
    public ResponseEntity<List<Veiculo>> listar(@PathVariable Long lojaId) {
        List<Veiculo> lista = service.listarPorLoja(lojaId);
        lista.forEach(item -> {
            item.add(Link.of("/api/lojas/" + lojaId + "/veiculos/" + item.getId(), "self"));
            item.add(Link.of("/api/lojas/" + lojaId, "loja"));
        });
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscar(@PathVariable Long lojaId, @PathVariable Long id) {
        Veiculo entity = service.buscarPorId(id);
        entity.add(Link.of("/api/lojas/" + lojaId + "/veiculos/" + id, "self"));
        entity.add(Link.of("/api/lojas/" + lojaId, "loja"));
        return ResponseEntity.ok(entity);
    }
    
    @PostMapping
    public ResponseEntity<Veiculo> criar(@PathVariable Long lojaId, @RequestBody Veiculo entity) {
        Veiculo novo = service.criar(lojaId, entity);
        novo.add(Link.of("/api/lojas/" + lojaId + "/veiculos/" + novo.getId(), "self"));
        return ResponseEntity.status(201).body(novo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Long lojaId, @PathVariable Long id, @RequestBody Veiculo entity) {
        Veiculo atualizado = service.atualizar(lojaId, id, entity);
        atualizado.add(Link.of("/api/lojas/" + lojaId + "/veiculos/" + id, "self"));
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
        return ResponseEntity.ok("Veiculo Service OK");
    }
}
