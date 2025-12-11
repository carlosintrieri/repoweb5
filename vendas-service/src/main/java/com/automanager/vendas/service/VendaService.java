package com.automanager.vendas.service;
import com.automanager.vendas.model.Venda;
import com.automanager.vendas.repository.VendaRepository;
import com.automanager.vendas.dto.VendaDetalhadaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.*;

@Service
public class VendaService {
    @Autowired private VendaRepository repository;
    @Autowired private RestTemplate restTemplate;
    
    @Value("${services.lojas}") private String lojasUrl;
    @Value("${services.clientes}") private String clientesUrl;
    @Value("${services.funcionarios}") private String funcionariosUrl;
    @Value("${services.veiculos}") private String veiculosUrl;
    
    public List<Venda> listarPorLoja(Long lojaId) {
        validarLoja(lojaId);
        return repository.findByLojaId(lojaId);
    }
    
    public Venda buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Venda não encontrada"));
    }
    
    public Venda criar(Long lojaId, Venda venda) {
        validarLoja(lojaId);
        venda.setLojaId(lojaId);
        return repository.save(venda);
    }
    
    public VendaDetalhadaDTO buscarDetalhada(Long lojaId, Long id) {
        Venda venda = buscarPorId(id);
        VendaDetalhadaDTO dto = new VendaDetalhadaDTO();
        dto.setId(venda.getId());
        dto.setValorTotal(venda.getValorTotal());
        dto.setFormaPagamento(venda.getFormaPagamento());
        dto.setStatus(venda.getStatus());
        dto.setDataVenda(venda.getDataVenda());
        dto.setObservacoes(venda.getObservacoes());
        
        try {
            dto.setLoja(restTemplate.getForObject(lojasUrl + "/api/lojas/" + lojaId, Map.class));
        } catch (Exception e) {}
        
        try {
            dto.setCliente(restTemplate.getForObject(clientesUrl + "/api/lojas/" + lojaId + "/clientes/" + venda.getClienteId(), Map.class));
        } catch (Exception e) {}
        
        try {
            dto.setFuncionario(restTemplate.getForObject(funcionariosUrl + "/api/lojas/" + lojaId + "/funcionarios/" + venda.getFuncionarioId(), Map.class));
        } catch (Exception e) {}
        
        try {
            dto.setVeiculo(restTemplate.getForObject(veiculosUrl + "/api/lojas/" + lojaId + "/veiculos/" + venda.getVeiculoId(), Map.class));
        } catch (Exception e) {}
        
        return dto;
    }
    
    public List<Venda> listarPorPeriodo(Long lojaId, LocalDate inicio, LocalDate fim) {
        validarLoja(lojaId);
        return repository.findByLojaIdAndDataVendaBetween(lojaId, inicio, fim);
    }
    
    public Long contar(Long lojaId) {
        return repository.countByLojaId(lojaId);
    }
    
    public Double totalFaturamento(Long lojaId) {
        Double total = repository.sumValorTotalByLojaId(lojaId);
        return total != null ? total : 0.0;
    }
    
    private void validarLoja(Long lojaId) {
        try {
            restTemplate.getForObject(lojasUrl + "/api/lojas/" + lojaId, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Loja não encontrada");
        }
    }
}
