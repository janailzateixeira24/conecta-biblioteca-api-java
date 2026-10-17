
package br.com.bibliotecaconectada.conecta_biblioteca_api_java;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "Biblioteca Conectada - API funcionando!";
    }

    @GetMapping("/healthz")
    public String health() {
        return "OK";
    }
}
