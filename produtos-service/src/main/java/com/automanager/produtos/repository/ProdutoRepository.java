package com.automanager.produtos.repository;
import com.automanager.produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByLojaId(Long lojaId);
    Long countByLojaId(Long lojaId);
}
