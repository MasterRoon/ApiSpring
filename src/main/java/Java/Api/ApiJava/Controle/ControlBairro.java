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
    public ResponseEntity<?> buscarBairros(
            @RequestParam(required = false) Long codigoBairro,
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status) {
        var bairros = bairroService.buscarBairros(codigoBairro, codigoMunicipio, nome, status);

        if(codigoBairro != null && bairros.size()==1){
            return ResponseEntity.ok(bairros.get(0));
        }

        return ResponseEntity.ok(bairros);
    }

    @PutMapping
    public ResponseEntity<List<BairroDto>> atualizarBairro(@RequestBody AtualizarBairro dto) {
        // Atualiza o bairro
        var bairrosAtualizados = bairroService.atualizarBairro(dto);

        return ResponseEntity.ok(bairrosAtualizados);
    }

    @DeleteMapping
    public ResponseEntity<List<BairroDto>> deletarMunicipio(@RequestBody AtualizarBairro codigoBairroDto) {

        List<BairroDto> bairroAtualizado = bairroService.deletarBairro(codigoBairroDto);

        // Retorna todos os registros da tabela após a operação
        return ResponseEntity.ok(bairroAtualizado);
    }

}
