package Java.Api.ApiJava.service;


import Java.Api.ApiJava.Controle.Dto.CadrastroEndereco;
import Java.Api.ApiJava.Controle.Dto.CriarPessoaDto;
import Java.Api.ApiJava.Controle.Dto.RespostaEndereco;
import Java.Api.ApiJava.Repositorio.EnderecoRepositorio;
import Java.Api.ApiJava.Repositorio.PessoaRepositorio;
import Java.Api.ApiJava.entity.Endereco;
import Java.Api.ApiJava.entity.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Service
public class EnderecoService {
    private PessoaRepositorio pessoaRepositorio;

    private EnderecoRepositorio enderecoRepositorio;

    @Autowired
    public EnderecoService(EnderecoRepositorio enderecoRepositorio, PessoaRepositorio pessoaRepositorio) {
        this.enderecoRepositorio = enderecoRepositorio;
        this.pessoaRepositorio = pessoaRepositorio;
    }

    public void cadastroEndereco(String codigoPessoa, CadrastroEndereco cadrastroEndereco) {

        var pessoa = pessoaRepositorio.findById(Long.parseLong(codigoPessoa))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var bairro = null;

        var endereco = new Endereco(
                cadrastroEndereco.codigoEndereco(),
                null,
                Instant.now(),
                cadrastroEndereco.cep(),            // cep
                cadrastroEndereco.complemento(),      // complemento (pode ser opcional)
                cadrastroEndereco.nomeRua(),          // nome da rua
                cadrastroEndereco.numero(),           // número
                bairro,
                pessoa
        );
        var enderecoCadrastrado = enderecoRepositorio.save(endereco);
    }

    public RespostaEndereco getEndereco(Long codigoEndereco) {
        // Busca o endereço pelo código e lança exceção caso não exista
        var endereco = enderecoRepositorio.findById(codigoEndereco)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

    }
}
