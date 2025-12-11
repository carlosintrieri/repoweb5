package com.automanager.lojas.repository;

import com.automanager.lojas.model.Loja;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LojaRepository extends JpaRepository<Loja, Long> {
    List<Loja> findByAtivaTrue();
}
