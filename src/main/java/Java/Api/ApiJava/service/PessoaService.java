package Java.Api.ApiJava.service;


import Java.Api.ApiJava.Controle.Dto.AtualizarDto;
import Java.Api.ApiJava.Controle.Dto.CadrastroEndereco;
import Java.Api.ApiJava.Controle.Dto.CriarPessoaDto;
import Java.Api.ApiJava.Repositorio.BairroRepositorio;
import Java.Api.ApiJava.Repositorio.EnderecoRepositorio;
import Java.Api.ApiJava.Repositorio.PessoaRepositorio;
import Java.Api.ApiJava.entity.Endereco;
import Java.Api.ApiJava.entity.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PessoaService {

    private final PessoaRepositorio pessoaRepositorio;
    private final EnderecoRepositorio enderecoRepositorio;
    private final BairroRepositorio bairroRepositorio;

    @Autowired
    public PessoaService(PessoaRepositorio pessoaRepositorio, EnderecoRepositorio enderecoRepositorio, BairroRepositorio bairroRepositorio) {
        this.pessoaRepositorio = pessoaRepositorio;
        this.enderecoRepositorio = enderecoRepositorio;
        this.bairroRepositorio = bairroRepositorio;
    }

    public Long cadastrarPessoaComEnderecos(CriarPessoaDto criarPessoaDto) {
        // Cria a entidade Pessoa
        var pessoa = new Pessoa(
                null,// código da pessoa, gerado automaticamente
                null,
                Instant.now(),
                new HashSet<>(), // endereços
                1, // status ativo
                criarPessoaDto.senha(),
                criarPessoaDto.login(),
                criarPessoaDto.idade(),
                criarPessoaDto.sobrenome(),
                criarPessoaDto.nome()
        );

        // Salva a pessoa primeiro, para obter o ID
        pessoaRepositorio.save(pessoa);

        // Processa e salva cada endereço
        for (CadrastroEndereco cadrastroEndereco  : criarPessoaDto.enderecos()) {
            var bairro = bairroRepositorio.findById(cadrastroEndereco.codigoBairro())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bairro não encontrado"));

            var endereco = new Endereco(
                    null,
                    null,
                    Instant.now(),
                    cadrastroEndereco.cep(),
                    cadrastroEndereco.complemento(),
                    cadrastroEndereco.nomeRua(),
                    cadrastroEndereco.numero(),
                    bairro,
                    pessoa
            );

            // Adiciona o endereço à lista de endereços da pessoa e salva
            pessoa.getEnderecos().add(endereco);
            enderecoRepositorio.save(endereco);
        }

        // Atualiza a pessoa com os endereços inseridos
        pessoaRepositorio.save(pessoa);

        // Retorna o ID da pessoa salva
        return pessoa.getCodigoPessoa();
    }

    public List<AtualizarDto> listarPessoasFiltradas(Long codigoPessoa, String login, Integer status) {
        List<Pessoa> pessoas;

        if (codigoPessoa != null) {
            // Busca por ID da pessoa
            pessoas = pessoaRepositorio.findById(codigoPessoa)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());

        } else if (login != null) {
            // Busca por login
            pessoas = pessoaRepositorio.findByLogin(login);

        } else if (status != null) {
            // Busca por status
            pessoas = pessoaRepositorio.findByStatus(status);

        } else {
            // Nenhum filtro especificado, busca todas as pessoas sem endereços
            pessoas = pessoaRepositorio.findAllWithoutEnderecos();
        }

        // Converte para AtualizarDto antes de retornar
        return pessoas.stream()
                .map(pessoa -> new AtualizarDto(
                        pessoa.getNome(),
                        pessoa.getSobrenome(),
                        pessoa.getIdade(),
                        pessoa.getLogin(),
                        pessoa.getSenha(),
                        pessoa.getStatus())
                )
                .collect(Collectors.toList());
    }




}
