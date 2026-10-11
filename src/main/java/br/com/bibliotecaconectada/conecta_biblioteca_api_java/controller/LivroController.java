package br.com.bibliotecaconectada.conecta_biblioteca_api_java.controller;

import br.com.bibliotecaconectada.conecta_biblioteca_api_java.model.Livro;
import br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
@CrossOrigin(origins = "*")
public class LivroController {

    private final LivroRepository repository;

    public LivroController(LivroRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Livro> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Livro buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Livro não encontrado"
            ));
    }

    @GetMapping("/buscar")
    public List<Livro> pesquisar(@RequestParam String termo) {
        return repository
            .findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase(
                termo, termo
            );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Livro cadastrar(@Valid @RequestBody Livro livro) {
        return repository.save(livro);
    }

    @PutMapping("/{id}")
    public Livro atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Livro dados) {

        Livro livro = buscarPorId(id);
        livro.setTitulo(dados.getTitulo());
        livro.setAutor(dados.getAutor());
        livro.setCategoria(dados.getCategoria());
        livro.setAnoPublicacao(dados.getAnoPublicacao());
        livro.setEditora(dados.getEditora());
        livro.setQuantidadeExemplares(dados.getQuantidadeExemplares());

        return repository.save(livro);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        Livro livro = buscarPorId(id);
        repository.delete(livro);
    }
}