package Java.Api.ApiJava.Controle;



import Java.Api.ApiJava.Controle.Dto.AtualizarMunicipio;
import Java.Api.ApiJava.Controle.Dto.InserirMunicipio;
import Java.Api.ApiJava.Controle.Dto.MunicipioDto;
import Java.Api.ApiJava.service.MunicipioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/municipio")
public class ControlMunicio {

    private final MunicipioService municipioService;

    public ControlMunicio(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    @PostMapping
    public ResponseEntity<List<MunicipioDto>> criarMunicipio(@RequestBody InserirMunicipio dto) {
        // Chama o serviço para criar o município
        List<MunicipioDto> municipios = municipioService.criarMunicipio(dto);

        // Retorna todos os registros da tabela
        return ResponseEntity.ok(municipios);
    }

    @GetMapping
    public ResponseEntity<?> buscarMunicipios(
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(name = "codigoUf", required = false) Long codigoUf, // Para "codigoUf"
            @RequestParam(name = "codigoUF", required = false) Long codigoUF,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status
    ) {

        if (codigoUF != null) {
            codigoUf = codigoUF;
        }

        var municipios = municipioService.buscarMunicipios(codigoMunicipio, codigoUf, nome, status);

        if (codigoMunicipio != null && municipios.size() == 1) {
            // Retorna o único município diretamente
            return ResponseEntity.ok(municipios.getFirst());
        }

        // Retorna a lista de municípios
        return ResponseEntity.ok(municipios);
    }

    @PutMapping
    public ResponseEntity<List<MunicipioDto>> atualizarMunicipio( @RequestBody AtualizarMunicipio dto) {

        // Chama o serviço para atualizar o município
        var municipiosAtualizados = municipioService.atualizarMunicipio(dto);

        // Retorna os municípios atualizados
        return ResponseEntity.ok(municipiosAtualizados);
    }

    @DeleteMapping
    public ResponseEntity<List<MunicipioDto>> deletarMunicipio(@RequestBody AtualizarMunicipio codigoMunicipioDto) {
        List<MunicipioDto> municipiosAtualizados = municipioService.deletarMunicipio(codigoMunicipioDto);

        // Retorna todos os registros da tabela após a operação
        return ResponseEntity.ok(municipiosAtualizados);
    }


}
