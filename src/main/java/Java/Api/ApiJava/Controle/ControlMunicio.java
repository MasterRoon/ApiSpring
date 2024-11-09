package Java.Api.ApiJava.Controle;


import Java.Api.ApiJava.Controle.Dto.AtualizarMunicipio;
import Java.Api.ApiJava.Controle.Dto.InserirMunicipio;
import Java.Api.ApiJava.entity.Municipio;
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
    public ResponseEntity<Municipio> cadastrarMunicipio(@RequestBody InserirMunicipio inserirMunicipio) {
        Municipio municipio = municipioService.criarMunicipio(inserirMunicipio);
        return ResponseEntity.ok(municipio);
    }

    @PutMapping("/{codigoMunicipio}")
    public ResponseEntity<Void> atualizarMunicipio(
            @PathVariable Long codigoMunicipio,
            @RequestBody AtualizarMunicipio atualizarMunicipio) {

        municipioService.atualizarMunicipios(codigoMunicipio, atualizarMunicipio);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Municipio>> buscarMunicipios(
            @RequestParam(required = false) Long codigoMunicipio,
            @RequestParam(required = false) Long codigoUf,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer status) {

        List<Municipio> municipios = municipioService.buscarMunicipios(codigoMunicipio, codigoUf, nome, status);
        return ResponseEntity.ok(municipios);
    }

}
