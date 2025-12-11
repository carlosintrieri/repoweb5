package com.automanager.lojas.dto;

public class LojaResumoDTO {
    private Long id;
    private String nome;
    private String cidade;
    private Long clientes;
    private Long funcionarios;
    private Long produtos;
    private Long vendas;
    private Long veiculos;
    private Double faturamento;
    
    public LojaResumoDTO() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public Long getClientes() { return clientes; }
    public void setClientes(Long clientes) { this.clientes = clientes; }
    public Long getFuncionarios() { return funcionarios; }
    public void setFuncionarios(Long funcionarios) { this.funcionarios = funcionarios; }
    public Long getProdutos() { return produtos; }
    public void setProdutos(Long produtos) { this.produtos = produtos; }
    public Long getVendas() { return vendas; }
    public void setVendas(Long vendas) { this.vendas = vendas; }
    public Long getVeiculos() { return veiculos; }
    public void setVeiculos(Long veiculos) { this.veiculos = veiculos; }
    public Double getFaturamento() { return faturamento; }
    public void setFaturamento(Double faturamento) { this.faturamento = faturamento; }
}
