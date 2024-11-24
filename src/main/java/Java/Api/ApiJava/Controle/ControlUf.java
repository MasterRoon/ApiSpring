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
    public ResponseEntity<?> buscarUfs(
            @RequestParam(required = false) Long codigoUf,
            @RequestParam(required = false) String sigla,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status
    ) {
        var ufs = service.buscarUfs(codigoUf, sigla, nome, status);

        // Se for busca por código e existir exatamente 1 resultado, retorna o objeto diretamente
        if (codigoUf != null && ufs.size() == 1) {
            return ResponseEntity.ok(ufs.get(0)); // Retorna o único UF encontrado
        }

        // Retorna uma lista de UFs ou uma lista vazia
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

        List<UfDto> ufsAtualizadas = service.deletarUf(codigoUf);

        // Retorna todos os registros da tabela após a operação
        return ResponseEntity.ok(ufsAtualizadas);
    }




}
