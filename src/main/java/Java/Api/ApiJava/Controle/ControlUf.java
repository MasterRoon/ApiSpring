package Java.Api.ApiJava.Controle;


import Java.Api.ApiJava.Controle.Dto.AtualizarUf;
import Java.Api.ApiJava.Controle.Dto.InserirUf;
import Java.Api.ApiJava.Controle.Dto.UfDto;
import Java.Api.ApiJava.service.UfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/uf")
public class ControlUf {

    private final UfService service;

    public ControlUf(UfService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<List<UfDto>> criarUf(@RequestBody InserirUf inserirUf) {
        var ufs = service.inserirUf(inserirUf);
        return ResponseEntity.ok(ufs);
    }

    @GetMapping
    public ResponseEntity<List<UfDto>> buscarUfs(
            @RequestParam(required = false) Long codigoUf,
            @RequestParam(required = false) String sigla,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status
    ) {
        var ufs = service.buscarUfs(codigoUf, sigla, nome, status);
        return ResponseEntity.ok(ufs);
    }

    @PutMapping
    public ResponseEntity<List<UfDto>> atualizarUf(@RequestBody AtualizarUf dto) {
        // Atualiza a UF e retorna todos os registros atualizados
        List<UfDto> ufsAtualizadas = service.atualizarUf(dto);

        // Retorna a resposta com todos os registros da tabela UF
        return ResponseEntity.ok(ufsAtualizadas);
    }

    @DeleteMapping
    public ResponseEntity<List<UfDto>> deletarUf(@RequestBody Long codigoUf) {
        // Chama o método de serviço para realizar a operação
        List<UfDto> ufsAtualizadas = service.deletarUf(codigoUf);

        // Retorna todos os registros da tabela após a operação
        return ResponseEntity.ok(ufsAtualizadas);
    }


}
