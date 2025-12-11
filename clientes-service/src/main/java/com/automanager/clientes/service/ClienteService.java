package com.automanager.clientes.service;
import com.automanager.clientes.model.Cliente;
import com.automanager.clientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class ClienteService {
    @Autowired private ClienteRepository repository;
    @Autowired private RestTemplate restTemplate;
    @Value("${services.lojas}") private String lojasUrl;
    
    public List<Cliente> listarPorLoja(Long lojaId) {
        validarLoja(lojaId);
        return repository.findByLojaId(lojaId);
    }
    
    public Cliente buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }
    
    public Cliente criar(Long lojaId, Cliente entity) {
        validarLoja(lojaId);
        entity.setLojaId(lojaId);
        return repository.save(entity);
    }
    
    public Cliente atualizar(Long lojaId, Long id, Cliente entity) {
        validarLoja(lojaId);
        Cliente existente = buscarPorId(id);
        entity.setId(id);
        entity.setLojaId(lojaId);
        return repository.save(entity);
    }
    
    public void deletar(Long id) {
        repository.deleteById(id);
    }
    
    public Long contar(Long lojaId) {
        return repository.countByLojaId(lojaId);
    }
    
    private void validarLoja(Long lojaId) {
        try {
            restTemplate.getForObject(lojasUrl + "/api/lojas/" + lojaId, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Loja não encontrada");
        }
    }
}
