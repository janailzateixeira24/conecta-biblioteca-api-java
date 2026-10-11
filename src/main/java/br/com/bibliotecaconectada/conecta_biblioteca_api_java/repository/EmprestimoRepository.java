package br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository;

import br.com.bibliotecaconectada.conecta_biblioteca_api_java.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository
        extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByUsuarioId(Long usuarioId);

    List<Emprestimo> findByStatus(String status);

    boolean existsByUsuarioIdAndStatus(Long usuarioId, String status);
}