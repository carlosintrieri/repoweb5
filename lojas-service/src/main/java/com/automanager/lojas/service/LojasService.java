package com.automanager.lojas.service;

import com.automanager.lojas.model.Loja;
import com.automanager.lojas.repository.LojaRepository;
import com.automanager.lojas.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LojasService {
    @Autowired private LojaRepository repository;
    @Autowired private RestTemplate restTemplate;

    @Value("${services.clientes}") private String clientesUrl;
    @Value("${services.funcionarios}") private String funcionariosUrl;
    @Value("${services.produtos}") private String produtosUrl;
    @Value("${services.vendas}") private String vendasUrl;
    @Value("${services.veiculos}") private String veiculosUrl;

    public List<Loja> listarTodas() {
        return repository.findAll();
    }

    public Loja buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Loja não encontrada"));
    }

    public Loja criar(Loja loja) {
        return repository.save(loja);
    }

    public Loja atualizar(Long id, Loja loja) {
        Loja existente = buscarPorId(id);
        existente.setNome(loja.getNome());
        existente.setCnpj(loja.getCnpj());
        existente.setEndereco(loja.getEndereco());
        existente.setCidade(loja.getCidade());
        existente.setEstado(loja.getEstado());
        existente.setTelefone(loja.getTelefone());
        existente.setEmail(loja.getEmail());
        return repository.save(existente);
    }

    public DashboardDTO getDashboardGeral() {
        DashboardDTO dash = new DashboardDTO();
        List<Loja> lojas = repository.findAll();
        dash.setTotalLojas(lojas.size());

        List<LojaResumoDTO> resumos = lojas.stream().map(loja -> {
            LojaResumoDTO r = new LojaResumoDTO();
            r.setId(loja.getId());
            r.setNome(loja.getNome());
            r.setCidade(loja.getCidade());
            try {
                Long[] counts = getContadoresLoja(loja.getId());
                r.setClientes(counts[0]);
                r.setFuncionarios(counts[1]);
                r.setProdutos(counts[2]);
                r.setVendas(counts[3]);
                r.setVeiculos(counts[4]);
                r.setFaturamento(counts[5].doubleValue());
            } catch (Exception e) {
                r.setClientes(0L);
                r.setFuncionarios(0L);
                r.setProdutos(0L);
                r.setVendas(0L);
                r.setVeiculos(0L);
                r.setFaturamento(0.0);
            }
            return r;
        }).collect(Collectors.toList());

        dash.setLojas(resumos);
        dash.setTotalClientes(resumos.stream().mapToLong(LojaResumoDTO::getClientes).sum());
        dash.setTotalFuncionarios(resumos.stream().mapToLong(LojaResumoDTO::getFuncionarios).sum());
        dash.setTotalProdutos(resumos.stream().mapToLong(LojaResumoDTO::getProdutos).sum());
        dash.setTotalVendas(resumos.stream().mapToLong(LojaResumoDTO::getVendas).sum());
        dash.setTotalVeiculos(resumos.stream().mapToLong(LojaResumoDTO::getVeiculos).sum());
        dash.setFaturamentoTotal(resumos.stream().mapToDouble(LojaResumoDTO::getFaturamento).sum());

        return dash;
    }

    private Long[] getContadoresLoja(Long lojaId) {
        Long[] counts = new Long[6];
        try {
            counts[0] = restTemplate.getForObject(clientesUrl + "/api/lojas/" + lojaId + "/clientes/count", Long.class);
        } catch (Exception e) { counts[0] = 0L; }
        try {
            counts[1] = restTemplate.getForObject(funcionariosUrl + "/api/lojas/" + lojaId + "/funcionarios/count", Long.class);
        } catch (Exception e) { counts[1] = 0L; }
        try {
            counts[2] = restTemplate.getForObject(produtosUrl + "/api/lojas/" + lojaId + "/produtos/count", Long.class);
        } catch (Exception e) { counts[2] = 0L; }
        try {
            counts[3] = restTemplate.getForObject(vendasUrl + "/api/lojas/" + lojaId + "/vendas/count", Long.class);
        } catch (Exception e) { counts[3] = 0L; }
        try {
            counts[4] = restTemplate.getForObject(veiculosUrl + "/api/lojas/" + lojaId + "/veiculos/count", Long.class);
        } catch (Exception e) { counts[4] = 0L; }
        try {
            counts[5] = restTemplate.getForObject(vendasUrl + "/api/lojas/" + lojaId + "/vendas/total", Long.class);
        } catch (Exception e) { counts[5] = 0L; }
        return counts;
    }
}
