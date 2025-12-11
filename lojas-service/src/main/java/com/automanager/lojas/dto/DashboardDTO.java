package com.automanager.lojas.dto;

import java.util.List;

public class DashboardDTO {
    private Integer totalLojas;
    private Long totalClientes;
    private Long totalFuncionarios;
    private Long totalProdutos;
    private Long totalVendas;
    private Long totalVeiculos;
    private Double faturamentoTotal;
    private List<LojaResumoDTO> lojas;
    
    public DashboardDTO() {}
    
    public Integer getTotalLojas() { return totalLojas; }
    public void setTotalLojas(Integer totalLojas) { this.totalLojas = totalLojas; }
    public Long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(Long totalClientes) { this.totalClientes = totalClientes; }
    public Long getTotalFuncionarios() { return totalFuncionarios; }
    public void setTotalFuncionarios(Long totalFuncionarios) { this.totalFuncionarios = totalFuncionarios; }
    public Long getTotalProdutos() { return totalProdutos; }
    public void setTotalProdutos(Long totalProdutos) { this.totalProdutos = totalProdutos; }
    public Long getTotalVendas() { return totalVendas; }
    public void setTotalVendas(Long totalVendas) { this.totalVendas = totalVendas; }
    public Long getTotalVeiculos() { return totalVeiculos; }
    public void setTotalVeiculos(Long totalVeiculos) { this.totalVeiculos = totalVeiculos; }
    public Double getFaturamentoTotal() { return faturamentoTotal; }
    public void setFaturamentoTotal(Double faturamentoTotal) { this.faturamentoTotal = faturamentoTotal; }
    public List<LojaResumoDTO> getLojas() { return lojas; }
    public void setLojas(List<LojaResumoDTO> lojas) { this.lojas = lojas; }
}
