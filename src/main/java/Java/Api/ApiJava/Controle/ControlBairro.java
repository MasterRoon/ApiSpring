package Java.Api.ApiJava.Controle;

import Java.Api.ApiJava.Controle.Dto.AtualizarBairro;
import Java.Api.ApiJava.Controle.Dto.InserirBairro;
import Java.Api.ApiJava.entity.Bairro;
import Java.Api.ApiJava.service.BairroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bairro")
public class ControlBairro {

    private final BairroService bairroService;

    public ControlBairro(BairroService bairroService) {
        this.bairroService = bairroService;
    }

    @PostMapping
    public ResponseEntity<Bairro> cadastrarBairro(@RequestBody InserirBairro inserirBairro) {
        Bairro bairro = bairroService.criarBairro(inserirBairro);
        return ResponseEntity.ok(bairro);
    }

    @PutMapping("/{codigoBairro}")
    public ResponseEntity<Void> atualizarBairro(
            @PathVariable Long codigoBairro,
            @RequestBody AtualizarBairro atualizarBairro) {

        bairroService.atualizarBairros(codigoBairro, atualizarBairro);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Bairro>> buscarBairros(
            @RequestParam(required = false) Long codigoBairro,
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status) {

        List<Bairro> bairros = bairroService.buscarBairros(codigoBairro, codigoMunicipio, nome, status);
        return ResponseEntity.ok(bairros);
    }

    @DeleteMapping("/{codigoBairro}")
    public ResponseEntity<Void> desativarBairro(@PathVariable Long codigoBairro) {
        bairroService.desativarBairro(codigoBairro);
        return ResponseEntity.noContent().build();
    }

}
