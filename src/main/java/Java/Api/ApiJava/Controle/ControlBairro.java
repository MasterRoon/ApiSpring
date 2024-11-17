package Java.Api.ApiJava.Controle;


import Java.Api.ApiJava.Controle.Dto.AtualizarBairro;
import Java.Api.ApiJava.Controle.Dto.BairroDto;
import Java.Api.ApiJava.Controle.Dto.InserirBairro;
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
    public ResponseEntity<List<BairroDto>> criarBairro(@RequestBody InserirBairro dto) {
        List<BairroDto> bairros = bairroService.inserirBairro(dto);
        return ResponseEntity.ok(bairros);
    }

    @GetMapping
    public ResponseEntity<List<BairroDto>> buscarBairros(
            @RequestParam(required = false) Long codigoBairro,
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status) {
        List<BairroDto> bairros = bairroService.buscarBairros(codigoBairro, codigoMunicipio, nome, status);
        return ResponseEntity.ok(bairros);
    }

    @PutMapping
    public ResponseEntity<List<BairroDto>> atualizarBairro(@RequestBody AtualizarBairro dto) {
        // Atualiza o bairro
        var bairrosAtualizados = bairroService.atualizarBairro(dto);

        return ResponseEntity.ok(bairrosAtualizados);
    }

    @DeleteMapping
    public ResponseEntity<List<BairroDto>> deletarMunicipio(@RequestBody Long codigoBairro) {

        List<BairroDto> bairroAtualizado = bairroService.deletarBairro(codigoBairro);

        // Retorna todos os registros da tabela após a operação
        return ResponseEntity.ok(bairroAtualizado);
    }

}
