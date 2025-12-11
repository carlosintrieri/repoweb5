package com.automanager.lojas.controller;

import com.automanager.lojas.model.Loja;
import com.automanager.lojas.service.LojasService;
import com.automanager.lojas.dto.DashboardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;

@RestController
@RequestMapping("/api/lojas")
public class LojasController {

    @Autowired
    private LojasService service;

    @GetMapping
    public ResponseEntity<List<Loja>> listar() {
        List<Loja> lojas = service.listarTodas();
        lojas.forEach(loja -> {
            loja.add(linkTo(methodOn(LojasController.class).buscar(loja.getId())).withSelfRel());
            loja.add(Link.of("/api/lojas/" + loja.getId() + "/dashboard", "dashboard"));
        });
        return ResponseEntity.ok(lojas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loja> buscar(@PathVariable Long id) {
        Loja loja = service.buscarPorId(id);
        loja.add(linkTo(methodOn(LojasController.class).buscar(id)).withSelfRel());
        loja.add(Link.of("/api/lojas/" + id + "/dashboard", "dashboard"));
        loja.add(Link.of("/api/lojas", "todas-lojas"));
        return ResponseEntity.ok(loja);
    }

    @PostMapping
    public ResponseEntity<Loja> criar(@RequestBody Loja loja) {
        Loja nova = service.criar(loja);
        nova.add(linkTo(methodOn(LojasController.class).buscar(nova.getId())).withSelfRel());
        return ResponseEntity.status(201).body(nova);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loja> atualizar(@PathVariable Long id, @RequestBody Loja loja) {
        Loja atualizada = service.atualizar(id, loja);
        atualizada.add(linkTo(methodOn(LojasController.class).buscar(id)).withSelfRel());
        return ResponseEntity.ok(atualizada);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> dashboardGeral() {
        return ResponseEntity.ok(service.getDashboardGeral());
    }

    @GetMapping("/{id}/dashboard")
    public ResponseEntity<DashboardDTO> dashboardLoja(@PathVariable Long id) {
        // Implementação simplificada - retorna dashboard apenas desta loja
        DashboardDTO dash = new DashboardDTO();
        dash.setTotalLojas(1);
        // Aqui você pode chamar os serviços para essa loja específica
        return ResponseEntity.ok(dash);
    }

    @GetMapping("/teste")
    public ResponseEntity<String> teste() {
        return ResponseEntity.ok("Lojas Service OK");
    }
}
