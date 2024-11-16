package Java.Api.ApiJava.Controle;


import Java.Api.ApiJava.Controle.Dto.AtualizarUf;
import Java.Api.ApiJava.Controle.Dto.InserirUf;
import Java.Api.ApiJava.Controle.Dto.UfDto;
import Java.Api.ApiJava.entity.Uf;
import Java.Api.ApiJava.service.UfService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @GetMapping("/{codigoUf}")
    public ResponseEntity<Uf> buscarUf(@PathVariable("codigoUf") String codigoUf) {
        var pessoa = service.getUfByCodigoUf(codigoUf);
        if(pessoa.isPresent()){
            return ResponseEntity.ok(pessoa.get());
        }else{
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping
    public ResponseEntity<List<Uf>> getAllUfs() {
        var lista = service.ListarUF();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{codigoUf}")
    public ResponseEntity<Void> atualizarUfCodigo(@PathVariable("codigoUf") String codigoUf, @RequestBody AtualizarUf atualizarUf){
        service.atualizarPessoaId(codigoUf, atualizarUf );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{codigoUf}")
    public ResponseEntity<Void> DesativarUf(@PathVariable Long codigoUf) {
        service.DesativarMunicipioByCodigoMunicipio(codigoUf);
        return ResponseEntity.noContent().build();
    }

}
