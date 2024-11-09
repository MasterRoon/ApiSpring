package Java.Api.ApiJava.Controle;

import Java.Api.ApiJava.Controle.Dto.CadrastroEndereco;
import Java.Api.ApiJava.service.EnderecoService;
import Java.Api.ApiJava.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enderecos")

public class ControlEndereco {

    private EnderecoService enderecoService;

    @Autowired
    public ControlEndereco(EnderecoService enderecoService, PessoaService pessoaService) {
        this.enderecoService = enderecoService;
    }

    @PostMapping("/{codigoPessoa}")
    public ResponseEntity<Void> cadrastroEndereco(@PathVariable String codigoPessoa, @RequestBody CadrastroEndereco cadrastroEndereco) {
        enderecoService.cadastroEndereco(codigoPessoa, cadrastroEndereco);
        return ResponseEntity.ok().build();
    }





}
