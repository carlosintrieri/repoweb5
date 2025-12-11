package com.automanager.vendas.repository;
import com.automanager.vendas.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByLojaId(Long lojaId);
    List<Venda> findByLojaIdAndDataVendaBetween(Long lojaId, LocalDate inicio, LocalDate fim);
    Long countByLojaId(Long lojaId);
    
    @Query("SELECT SUM(v.valorTotal) FROM Venda v WHERE v.lojaId = ?1")
    Double sumValorTotalByLojaId(Long lojaId);
}
