package Java.Api.ApiJava.Controle;


import Java.Api.ApiJava.Controle.Dto.AtualizarPessoaDto;
import Java.Api.ApiJava.Controle.Dto.CriarPessoaDto;
import Java.Api.ApiJava.Controle.Dto.EnderecoRespostaDto;
import Java.Api.ApiJava.Controle.Dto.PessoaRespostaDto;
import Java.Api.ApiJava.entity.Pessoa;
import Java.Api.ApiJava.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping
    public ResponseEntity<?> buscarPessoas(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) Long codigoPessoa) {

        // Se codigoPessoa for fornecido, retorna uma pessoa com endereços
        if (codigoPessoa != null) {
            Pessoa pessoa = pessoaService.buscarPorCodigoPessoa(codigoPessoa);
            PessoaRespostaDto pessoaDto = new PessoaRespostaDto(
                    pessoa.getCodigoPessoa(),
                    pessoa.getNome(),
                    pessoa.getSobrenome(),
                    pessoa.getIdade(),
                    pessoa.getLogin(),
                    pessoa.getStatus(),
                    pessoa.getEnderecos().stream().map(endereco -> new EnderecoRespostaDto(
                                    endereco.getCodigoEndereco(),
                                    endereco.getBairro().getCodigoBairro(),
                                    endereco.getNomeRua(),
                                    String.valueOf(endereco.getNumero()),
                                    endereco.getComplemento(),
                                    endereco.getCep()))
                            .collect(Collectors.toList())
            );
            return ResponseEntity.ok(pessoaDto);
        }

        // Caso contrário, retorna lista de pessoas (sem endereços)
        List<Pessoa> pessoas = pessoaService.buscarPorStatusOuLogin(status, login);
        List<PessoaRespostaDto> pessoasDto = pessoas.stream()
                .map(pessoa -> new PessoaRespostaDto(
                        pessoa.getCodigoPessoa(),
                        pessoa.getNome(),
                        pessoa.getSobrenome(),
                        pessoa.getIdade(),
                        pessoa.getLogin(),
                        pessoa.getStatus(),
                        Collections.emptyList())) // Endereços vazios
                .collect(Collectors.toList());

        return ResponseEntity.ok(pessoasDto);
    }

    @PutMapping
    public ResponseEntity<PessoaRespostaDto> atualizarPessoa(@RequestBody AtualizarPessoaDto pessoaDto) {
        Pessoa pessoaAtualizada = pessoaService.atualizarPessoaComEnderecos(pessoaDto);
        PessoaRespostaDto pessoaRespostaDto = new PessoaRespostaDto(
                pessoaAtualizada.getCodigoPessoa(),
                pessoaAtualizada.getNome(),
                pessoaAtualizada.getSobrenome(),
                pessoaAtualizada.getIdade(),
                pessoaAtualizada.getLogin(),
                pessoaAtualizada.getStatus(),
                pessoaAtualizada.getEnderecos().stream().map(endereco -> new EnderecoRespostaDto(
                                endereco.getCodigoEndereco(),
                                endereco.getBairro().getCodigoBairro(),
                                endereco.getNomeRua(),
                                String.valueOf(endereco.getNumero()),
                                endereco.getComplemento(),
                                endereco.getCep()))
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(pessoaRespostaDto);
    }


}
