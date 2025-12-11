package com.automanager.veiculos.service;
import com.automanager.veiculos.model.Veiculo;
import com.automanager.veiculos.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class VeiculoService {
    @Autowired private VeiculoRepository repository;
    @Autowired private RestTemplate restTemplate;
    @Value("${services.lojas}") private String lojasUrl;
    
    public List<Veiculo> listarPorLoja(Long lojaId) {
        validarLoja(lojaId);
        return repository.findByLojaId(lojaId);
    }
    
    public Veiculo buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Veiculo não encontrado"));
    }
    
    public Veiculo criar(Long lojaId, Veiculo entity) {
        validarLoja(lojaId);
        entity.setLojaId(lojaId);
        return repository.save(entity);
    }
    
    public Veiculo atualizar(Long lojaId, Long id, Veiculo entity) {
        validarLoja(lojaId);
        Veiculo existente = buscarPorId(id);
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
