package com.automanager.veiculos.repository;
import com.automanager.veiculos.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    List<Veiculo> findByLojaId(Long lojaId);
    Long countByLojaId(Long lojaId);
}
