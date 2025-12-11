package com.automanager.vendas.controller;
import com.automanager.vendas.model.Venda;
import com.automanager.vendas.service.VendaService;
import com.automanager.vendas.dto.VendaDetalhadaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lojas/{lojaId}/vendas")
public class VendaController {
    @Autowired private VendaService service;
    
    @GetMapping
    public ResponseEntity<List<Venda>> listar(@PathVariable Long lojaId) {
        List<Venda> lista = service.listarPorLoja(lojaId);
        lista.forEach(v -> {
            v.add(Link.of("/api/lojas/" + lojaId + "/vendas/" + v.getId(), "self"));
            v.add(Link.of("/api/lojas/" + lojaId + "/vendas/" + v.getId() + "/detalhada", "detalhada"));
        });
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscar(@PathVariable Long lojaId, @PathVariable Long id) {
        Venda venda = service.buscarPorId(id);
        venda.add(Link.of("/api/lojas/" + lojaId + "/vendas/" + id, "self"));
        venda.add(Link.of("/api/lojas/" + lojaId + "/vendas/" + id + "/detalhada", "detalhada"));
        return ResponseEntity.ok(venda);
    }
    
    @GetMapping("/{id}/detalhada")
    public ResponseEntity<VendaDetalhadaDTO> buscarDetalhada(@PathVariable Long lojaId, @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarDetalhada(lojaId, id));
    }
    
    @PostMapping
    public ResponseEntity<Venda> criar(@PathVariable Long lojaId, @RequestBody Venda venda) {
        Venda nova = service.criar(lojaId, venda);
        nova.add(Link.of("/api/lojas/" + lojaId + "/vendas/" + nova.getId(), "self"));
        return ResponseEntity.status(201).body(nova);
    }
    
    @GetMapping("/periodo")
    public ResponseEntity<List<Venda>> listarPorPeriodo(
            @PathVariable Long lojaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.listarPorPeriodo(lojaId, inicio, fim));
    }
    
    @GetMapping("/count")
    public ResponseEntity<Long> contar(@PathVariable Long lojaId) {
        return ResponseEntity.ok(service.contar(lojaId));
    }
    
    @GetMapping("/total")
    public ResponseEntity<Double> totalFaturamento(@PathVariable Long lojaId) {
        return ResponseEntity.ok(service.totalFaturamento(lojaId));
    }
    
    @GetMapping("/teste")
    public ResponseEntity<String> teste() {
        return ResponseEntity.ok("Vendas Service OK");
    }
}
