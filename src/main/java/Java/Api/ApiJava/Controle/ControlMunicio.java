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
    public ResponseEntity<List<MunicipioDto>> buscarMunicipios(
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) Long codigoUf,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status
    ) {
        var municipios = municipioService.buscarMunicipios(codigoMunicipio, codigoUf, nome, status);
        return ResponseEntity.ok(municipios);
    }

    @PutMapping
    public ResponseEntity<List<MunicipioDto>> atualizarMunicipio(@RequestBody AtualizarMunicipio dto) {
        // Chama o serviço para atualizar o município
        var municipiosAtualizados = municipioService.atualizarMunicipio(dto);

        // Retorna os municípios atualizados
        return ResponseEntity.ok(municipiosAtualizados);
    }

}
