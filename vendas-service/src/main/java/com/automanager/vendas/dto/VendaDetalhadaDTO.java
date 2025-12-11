package com.automanager.vendas.dto;
import java.time.LocalDate;
import java.util.Map;

public class VendaDetalhadaDTO {
    private Long id;
    private Double valorTotal;
    private String formaPagamento;
    private String status;
    private LocalDate dataVenda;
    private String observacoes;
    private Map<String, Object> loja;
    private Map<String, Object> cliente;
    private Map<String, Object> funcionario;
    private Map<String, Object> veiculo;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDate dataVenda) { this.dataVenda = dataVenda; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public Map<String, Object> getLoja() { return loja; }
    public void setLoja(Map<String, Object> loja) { this.loja = loja; }
    public Map<String, Object> getCliente() { return cliente; }
    public void setCliente(Map<String, Object> cliente) { this.cliente = cliente; }
    public Map<String, Object> getFuncionario() { return funcionario; }
    public void setFuncionario(Map<String, Object> funcionario) { this.funcionario = funcionario; }
    public Map<String, Object> getVeiculo() { return veiculo; }
    public void setVeiculo(Map<String, Object> veiculo) { this.veiculo = veiculo; }
}
