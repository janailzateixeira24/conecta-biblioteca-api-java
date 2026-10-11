package br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository;

import br.com.bibliotecaconectada.conecta_biblioteca_api_java.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
}
