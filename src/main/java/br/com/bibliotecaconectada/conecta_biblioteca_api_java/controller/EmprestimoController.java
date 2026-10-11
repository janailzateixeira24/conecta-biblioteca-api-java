package br.com.bibliotecaconectada.conecta_biblioteca_api_java.controller;

import br.com.bibliotecaconectada.conecta_biblioteca_api_java.model.Emprestimo;
import br.com.bibliotecaconectada.conecta_biblioteca_api_java.model.Livro;
import br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository.EmprestimoRepository;
import br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository.LivroRepository;
import br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
@CrossOrigin(origins = "*")
public class EmprestimoController {

    private final EmprestimoRepository emprestimos;
    private final LivroRepository livros;
    private final UsuarioRepository usuarios;

    public EmprestimoController(
            EmprestimoRepository emprestimos,
            LivroRepository livros,
            UsuarioRepository usuarios) {
        this.emprestimos = emprestimos;
        this.livros = livros;
        this.usuarios = usuarios;
    }

    @GetMapping
    public List<Emprestimo> listar() {
        return emprestimos.findAll();
    }

    @GetMapping("/ativos")
    public List<Emprestimo> listarAtivos() {
        return emprestimos.findByStatus("ATIVO");
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Emprestimo> listarPorUsuario(
            @PathVariable Long usuarioId) {
        if (!usuarios.existsById(usuarioId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuário não encontrado"
            );
        }
        return emprestimos.findByUsuarioId(usuarioId);
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public Emprestimo cadastrar(@RequestBody Emprestimo dados) {

        if (dados.getLivroId() == null || dados.getUsuarioId() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Informe livroId e usuarioId"
            );
        }

        if (!usuarios.existsById(dados.getUsuarioId())) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuário não encontrado"
            );
        }

        if (emprestimos.existsByUsuarioIdAndStatus(
                dados.getUsuarioId(), "ATIVO")) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "O usuário possui um empréstimo ativo"
            );
        }

        Livro livro = livros.findById(dados.getLivroId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Livro não encontrado"
            ));

        if (livro.getQuantidadeExemplares() == null
                || livro.getQuantidadeExemplares() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Não há exemplares disponíveis desse livro"
            );
        }

        LocalDate hoje = LocalDate.now();

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivroId(dados.getLivroId());
        emprestimo.setUsuarioId(dados.getUsuarioId());
        emprestimo.setDataEmprestimo(hoje);
        emprestimo.setDataPrevistaDevolucao(hoje.plusDays(7));
        emprestimo.setStatus("ATIVO");

        livro.setQuantidadeExemplares(
            livro.getQuantidadeExemplares() - 1
        );

        livros.save(livro);
        return emprestimos.save(emprestimo);
    }

    @PostMapping("/{id}/devolucao")
    @Transactional
    public Emprestimo devolver(@PathVariable Long id) {

        Emprestimo emprestimo = emprestimos.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Empréstimo não encontrado"
            ));

        if (!"ATIVO".equals(emprestimo.getStatus())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Este empréstimo já foi devolvido"
            );
        }

        Livro livro = livros.findById(emprestimo.getLivroId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Livro associado não encontrado"
            ));

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus("DEVOLVIDO");

        livro.setQuantidadeExemplares(
            livro.getQuantidadeExemplares() + 1
        );

        livros.save(livro);
        return emprestimos.save(emprestimo);
    }
}