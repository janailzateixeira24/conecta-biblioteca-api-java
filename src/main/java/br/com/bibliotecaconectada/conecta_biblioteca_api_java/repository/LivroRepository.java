package br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository;

import br.com.bibliotecaconectada.conecta_biblioteca_api_java.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase(
        String titulo, String autor
    );
}