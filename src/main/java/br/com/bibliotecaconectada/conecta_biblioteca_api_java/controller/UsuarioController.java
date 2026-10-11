package br.com.bibliotecaconectada.conecta_biblioteca_api_java.controller;

import br.com.bibliotecaconectada.conecta_biblioteca_api_java.model.Usuario;
import br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Usuario> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuário não encontrado"
            ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario cadastrar(@Valid @RequestBody Usuario usuario) {
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Já existe um usuário com esse e-mail"
            );
        }

        return repository.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario dados) {

        Usuario usuario = buscarPorId(id);

        boolean emailUsado = repository.existsByEmail(dados.getEmail());
        boolean mesmoEmail = usuario.getEmail().equalsIgnoreCase(dados.getEmail());

        if (emailUsado && !mesmoEmail) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Esse e-mail já está cadastrado"
            );
        }

        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setTelefone(dados.getTelefone());
        usuario.setTipo(dados.getTipo());

        return repository.save(usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        repository.delete(buscarPorId(id));
    }
}