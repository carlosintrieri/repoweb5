package com.automanager.clientes.repository;
import com.automanager.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByLojaId(Long lojaId);
    Long countByLojaId(Long lojaId);
}
