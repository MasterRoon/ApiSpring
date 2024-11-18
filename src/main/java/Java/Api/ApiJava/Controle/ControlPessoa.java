package Java.Api.ApiJava.Controle;

import Java.Api.ApiJava.Controle.Dto.AtualizarDto;
import Java.Api.ApiJava.Controle.Dto.CriarPessoaDto;
import Java.Api.ApiJava.Controle.Dto.PessoaRespostaDto;
import Java.Api.ApiJava.entity.Pessoa;
import Java.Api.ApiJava.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pessoa")

public class ControlPessoa {

    private final PessoaService pessoaService;

    @Autowired
    public ControlPessoa(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<List<PessoaRespostaDto>> criarPessoa(@RequestBody  CriarPessoaDto pessoaDto) {
        // Salvar a pessoa com endereços
        pessoaService.salvarPessoaComEnderecos(pessoaDto);

        // Retornar todas as pessoas
        List<PessoaRespostaDto> pessoasDto = pessoaService.listarTodasAsPessoas().stream()
                .map(pessoa -> new PessoaRespostaDto(
                        pessoa.getCodigoPessoa(),
                        pessoa.getNome(),
                        pessoa.getSobrenome(),
                        pessoa.getIdade(),
                        pessoa.getLogin(),
                        pessoa.getStatus(),
                        Collections.emptyList() // Lista de endereços vazia
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(pessoasDto);
    }

}
