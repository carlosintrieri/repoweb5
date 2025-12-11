package com.automanager.clientes.controller;
import com.automanager.clientes.model.Cliente;
import com.automanager.clientes.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lojas/{lojaId}/clientes")
public class ClienteController {
    @Autowired private ClienteService service;
    
    @GetMapping
    public ResponseEntity<List<Cliente>> listar(@PathVariable Long lojaId) {
        List<Cliente> lista = service.listarPorLoja(lojaId);
        lista.forEach(item -> {
            item.add(Link.of("/api/lojas/" + lojaId + "/clientes/" + item.getId(), "self"));
            item.add(Link.of("/api/lojas/" + lojaId, "loja"));
        });
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long lojaId, @PathVariable Long id) {
        Cliente entity = service.buscarPorId(id);
        entity.add(Link.of("/api/lojas/" + lojaId + "/clientes/" + id, "self"));
        entity.add(Link.of("/api/lojas/" + lojaId, "loja"));
        return ResponseEntity.ok(entity);
    }
    
    @PostMapping
    public ResponseEntity<Cliente> criar(@PathVariable Long lojaId, @RequestBody Cliente entity) {
        Cliente novo = service.criar(lojaId, entity);
        novo.add(Link.of("/api/lojas/" + lojaId + "/clientes/" + novo.getId(), "self"));
        return ResponseEntity.status(201).body(novo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long lojaId, @PathVariable Long id, @RequestBody Cliente entity) {
        Cliente atualizado = service.atualizar(lojaId, id, entity);
        atualizado.add(Link.of("/api/lojas/" + lojaId + "/clientes/" + id, "self"));
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
        return ResponseEntity.ok("Cliente Service OK");
    }
}
