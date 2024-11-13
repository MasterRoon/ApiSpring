package Java.Api.ApiJava.Controle;

import Java.Api.ApiJava.Controle.Dto.AtualizarDto;
import Java.Api.ApiJava.Controle.Dto.CriarPessoaDto;
import Java.Api.ApiJava.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pessoa")

public class ControlPessoa {

    private final PessoaService pessoaService;

    @Autowired
    public ControlPessoa(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarPessoaComEnderecos(@RequestBody CriarPessoaDto criarPessoaDto) {
        Long pessoaId = pessoaService.cadastrarPessoaComEnderecos(criarPessoaDto);
        return ResponseEntity.created(URI.create("/pessoa/" + pessoaId)).build();
    }

    @GetMapping
    public ResponseEntity<List<AtualizarDto>> listarPessoas(
            @RequestParam(required = false) Long codigoPessoa,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) Integer status) {

        var pessoas = pessoaService.listarPessoasFiltradas(codigoPessoa, login, status);
        return ResponseEntity.ok(pessoas);
    }

    @PutMapping("/{codigoPessoa}")
    public ResponseEntity<Void> atualizarPessoa(@PathVariable Long codigoPessoa, @RequestBody CriarPessoaDto criarPessoaDto) {
        pessoaService.atualizarPessoa(criarPessoaDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{codigoPessoa}")
    public ResponseEntity<Void> desativarPessoa(@PathVariable Long codigoPessoa) {
        pessoaService.desativarPessoa(codigoPessoa);
        return ResponseEntity.noContent().build();
    }

}
