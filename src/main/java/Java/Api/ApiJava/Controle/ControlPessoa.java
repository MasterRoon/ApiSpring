package Java.Api.ApiJava.Controle;

import Java.Api.ApiJava.Controle.Dto.AtualizarDto;
import Java.Api.ApiJava.Controle.Dto.CriarPessoaDto;
import Java.Api.ApiJava.service.PessoaService;
import org.springframework.web.bind.annotation.GetMapping;
import Java.Api.ApiJava.entity.Pessoa;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pessoas")

public class ControlPessoa {

    private final PessoaService pessoaService;

    public ControlPessoa(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<Pessoa> createUser(@RequestBody CriarPessoaDto criarPessoaDto){
        var UsuarioId = pessoaService.createPessoa(criarPessoaDto);
        return ResponseEntity.created(URI.create("/pessoas" + Long.valueOf(UsuarioId).toString())).build();
    }

    @GetMapping("/{codigoPessoa}")
    public ResponseEntity<Pessoa> getPessoaByCodigo(@PathVariable("codigoPessoa") String codigoPessoa){
        var pessoa = pessoaService.getPessoaByCodigoPessoa(codigoPessoa);
        if(pessoa.isPresent()){
            return ResponseEntity.ok(pessoa.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> getAllPessoas(){
        var lista = pessoaService.ListarPessoas();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{codigoPessoa}")
    public ResponseEntity<Void> atualizarPessoaCodigo(@PathVariable("codigoPessoa") String codigoPessoa, @RequestBody AtualizarDto atualizarDto){
        pessoaService.atualizarPessoaId(codigoPessoa, atualizarDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{codigoPessoa}")
    public ResponseEntity<Void> DeletePessoa(@PathVariable Long codigoPessoa) {
        pessoaService.DeletePessoaByCodigoPessoa(codigoPessoa);
        return ResponseEntity.noContent().build();
    }


}
