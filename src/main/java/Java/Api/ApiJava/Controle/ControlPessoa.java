package Java.Api.ApiJava.Controle;

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

    private PessoaService pessoaService;

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


}
