package Java.Api.ApiJava.service;



import Java.Api.ApiJava.Controle.Dto.CriarPessoaDto;
import Java.Api.ApiJava.Repositorio.BairroRepositorio;
import Java.Api.ApiJava.Repositorio.PessoaRepositorio;
import Java.Api.ApiJava.entity.Bairro;
import Java.Api.ApiJava.entity.Endereco;
import Java.Api.ApiJava.entity.Pessoa;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class PessoaService {

    private final PessoaRepositorio pessoaRepositorio;
    private final BairroRepositorio bairroRepositorio;

    @Autowired
    public PessoaService(PessoaRepositorio pessoaRepositorio, BairroRepositorio bairroRepositorio) {
        this.pessoaRepositorio = pessoaRepositorio;
        this.bairroRepositorio = bairroRepositorio;
    }


    @Transactional
    public void salvarPessoaComEnderecos(CriarPessoaDto pessoaDto) {
        // Cria a entidade Pessoa
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaDto.nome());
        pessoa.setSobrenome(pessoaDto.sobrenome());
        pessoa.setIdade(pessoaDto.idade());
        pessoa.setLogin(pessoaDto.login());
        pessoa.setSenha(pessoaDto.senha());
        pessoa.setStatus(pessoaDto.status());

        // Processa os endereços
        List<Endereco> enderecos = pessoaDto.enderecos().stream().map(enderecoDto -> {
            Bairro bairro = bairroRepositorio.findById(enderecoDto.codigoBairro())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bairro não encontrado"));

            Endereco endereco = new Endereco();
            endereco.setPessoa(pessoa);
            endereco.setBairro(bairro);
            endereco.setNomeRua(enderecoDto.nomeRua());
            endereco.setNumero(Integer.valueOf(enderecoDto.numero()));
            endereco.setComplemento(enderecoDto.complemento());
            endereco.setCep(enderecoDto.cep());
            return endereco;
        }).collect(Collectors.toList());

        pessoa.setEnderecos(enderecos);

        // Salva a Pessoa com seus Endereços
        pessoaRepositorio.save(pessoa);
    }

    public List<Pessoa> listarTodasAsPessoas() {
        return pessoaRepositorio.findAll();
    }

}
