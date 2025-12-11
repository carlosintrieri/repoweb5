package com.automanager.produtos.controller;
import com.automanager.produtos.model.Produto;
import com.automanager.produtos.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lojas/{lojaId}/produtos")
public class ProdutoController {
    @Autowired private ProdutoService service;
    
    @GetMapping
    public ResponseEntity<List<Produto>> listar(@PathVariable Long lojaId) {
        List<Produto> lista = service.listarPorLoja(lojaId);
        lista.forEach(item -> {
            item.add(Link.of("/api/lojas/" + lojaId + "/produtos/" + item.getId(), "self"));
            item.add(Link.of("/api/lojas/" + lojaId, "loja"));
        });
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscar(@PathVariable Long lojaId, @PathVariable Long id) {
        Produto entity = service.buscarPorId(id);
        entity.add(Link.of("/api/lojas/" + lojaId + "/produtos/" + id, "self"));
        entity.add(Link.of("/api/lojas/" + lojaId, "loja"));
        return ResponseEntity.ok(entity);
    }
    
    @PostMapping
    public ResponseEntity<Produto> criar(@PathVariable Long lojaId, @RequestBody Produto entity) {
        Produto novo = service.criar(lojaId, entity);
        novo.add(Link.of("/api/lojas/" + lojaId + "/produtos/" + novo.getId(), "self"));
        return ResponseEntity.status(201).body(novo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long lojaId, @PathVariable Long id, @RequestBody Produto entity) {
        Produto atualizado = service.atualizar(lojaId, id, entity);
        atualizado.add(Link.of("/api/lojas/" + lojaId + "/produtos/" + id, "self"));
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
        return ResponseEntity.ok("Produto Service OK");
    }
}
