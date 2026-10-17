
package br.com.bibliotecaconectada;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> inicio() {
        return Map.of(
            "projeto", "Biblioteca Conectada",
            "status", "API funcionando"
        );
    }

    @GetMapping("/healthz")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
