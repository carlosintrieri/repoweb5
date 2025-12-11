package com.automanager.funcionarios.repository;
import com.automanager.funcionarios.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByLojaId(Long lojaId);
    Long countByLojaId(Long lojaId);
}
