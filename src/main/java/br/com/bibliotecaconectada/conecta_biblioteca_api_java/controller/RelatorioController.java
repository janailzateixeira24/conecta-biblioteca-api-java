package br.com.bibliotecaconectada.conecta_biblioteca_api_java.controller;

import br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository.EmprestimoRepository;
import br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository.LivroRepository;
import br.com.bibliotecaconectada.conecta_biblioteca_api_java.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final LivroRepository livros;
    private final UsuarioRepository usuarios;
    private final EmprestimoRepository emprestimos;

    public RelatorioController(
            LivroRepository livros,
            UsuarioRepository usuarios,
            EmprestimoRepository emprestimos) {
        this.livros = livros;
        this.usuarios = usuarios;
        this.emprestimos = emprestimos;
    }

    @GetMapping("/resumo")
    public Map<String, Long> resumo() {
        Map<String, Long> resultado = new LinkedHashMap<>();

        resultado.put("totalLivros", livros.count());
        resultado.put("totalUsuarios", usuarios.count());
        resultado.put("totalEmprestimos", emprestimos.count());
        resultado.put(
            "emprestimosAtivos",
            (long) emprestimos.findByStatus("ATIVO").size()
        );
        resultado.put(
            "emprestimosDevolvidos",
            (long) emprestimos.findByStatus("DEVOLVIDO").size()
        );

        return resultado;
    }
}