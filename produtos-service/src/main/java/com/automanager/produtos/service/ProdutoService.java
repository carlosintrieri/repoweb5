package com.automanager.produtos.service;
import com.automanager.produtos.model.Produto;
import com.automanager.produtos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class ProdutoService {
    @Autowired private ProdutoRepository repository;
    @Autowired private RestTemplate restTemplate;
    @Value("${services.lojas}") private String lojasUrl;
    
    public List<Produto> listarPorLoja(Long lojaId) {
        validarLoja(lojaId);
        return repository.findByLojaId(lojaId);
    }
    
    public Produto buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }
    
    public Produto criar(Long lojaId, Produto entity) {
        validarLoja(lojaId);
        entity.setLojaId(lojaId);
        return repository.save(entity);
    }
    
    public Produto atualizar(Long lojaId, Long id, Produto entity) {
        validarLoja(lojaId);
        Produto existente = buscarPorId(id);
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
