package com.automanager.funcionarios.service;
import com.automanager.funcionarios.model.Funcionario;
import com.automanager.funcionarios.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class FuncionarioService {
    @Autowired private FuncionarioRepository repository;
    @Autowired private RestTemplate restTemplate;
    @Value("${services.lojas}") private String lojasUrl;
    
    public List<Funcionario> listarPorLoja(Long lojaId) {
        validarLoja(lojaId);
        return repository.findByLojaId(lojaId);
    }
    
    public Funcionario buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Funcionario não encontrado"));
    }
    
    public Funcionario criar(Long lojaId, Funcionario entity) {
        validarLoja(lojaId);
        entity.setLojaId(lojaId);
        return repository.save(entity);
    }
    
    public Funcionario atualizar(Long lojaId, Long id, Funcionario entity) {
        validarLoja(lojaId);
        Funcionario existente = buscarPorId(id);
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
