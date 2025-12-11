package com.automanager.veiculos.model;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
@Entity
public class Veiculo extends RepresentationModel<Veiculo> {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marca, modelo, placa, chassi, cor, tipo, combustivel, status;
    private Integer ano, quilometragem;
    private Double preco;
    private Long lojaId;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getChassi() { return chassi; }
    public void setChassi(String chassi) { this.chassi = chassi; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getCombustivel() { return combustivel; }
    public void setCombustivel(String combustivel) { this.combustivel = combustivel; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }
    public Integer getQuilometragem() { return quilometragem; }
    public void setQuilometragem(Integer quilometragem) { this.quilometragem = quilometragem; }
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    public Long getLojaId() { return lojaId; }
    public void setLojaId(Long lojaId) { this.lojaId = lojaId; }
}