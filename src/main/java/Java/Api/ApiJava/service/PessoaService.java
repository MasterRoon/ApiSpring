package Java.Api.ApiJava.service;


import Java.Api.ApiJava.Controle.Dto.AtualizarDto;
import Java.Api.ApiJava.Controle.Dto.CadrastroEndereco;
import Java.Api.ApiJava.Controle.Dto.CriarPessoaDto;
import Java.Api.ApiJava.Repositorio.BairroRepositorio;
import Java.Api.ApiJava.Repositorio.EnderecoRepositorio;
import Java.Api.ApiJava.Repositorio.PessoaRepositorio;
import Java.Api.ApiJava.entity.Bairro;
import Java.Api.ApiJava.entity.Endereco;
import Java.Api.ApiJava.entity.Pessoa;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public void salvarPessoa(CriarPessoaDto criarPessoaDto) {
        // Cria a entidade Pessoa
        Pessoa pessoa = new Pessoa(
                null,
                null,
                Instant.now(),
                new HashSet<>(),
                1,
                criarPessoaDto.nome(),
                criarPessoaDto.sobrenome(),
                criarPessoaDto.idade(),
                criarPessoaDto.login(),
                criarPessoaDto.senha()
        );

        // Processa os endereços
        List<Endereco> enderecos = criarPessoaDto.enderecos().stream()
                .map(enderecoDto -> {
                    var bairro = bairroRepositorio.findById(enderecoDto.codigoBairro())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bairro não encontrado"));

                    // Verifica se o bairro possui um código de município válido
                    if (bairro.getMunicipio() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O bairro informado não possui um município associado.");
                    }

                    return new Endereco(
                            null,
                            null,
                            Instant.now(),
                            enderecoDto.nomeRua(),
                            enderecoDto.cep(),
                            enderecoDto.complemento(),
                            enderecoDto.numero(),
                            bairro,
                            pessoa
                    );
                })
                .collect(Collectors.toList());

        pessoa.setEnderecos(new HashSet<>(enderecos));
        pessoaRepositorio.save(pessoa);
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

    @Transactional
    public void atualizarPessoa(CriarPessoaDto criarPessoaDto) {
        // Buscar a pessoa a partir do código fornecido
        var pessoa = pessoaRepositorio.findById(criarPessoaDto.codigoPessoa())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));

        // Atualizar os dados básicos da pessoa
        pessoa.setNome(criarPessoaDto.nome());
        pessoa.setSobrenome(criarPessoaDto.sobrenome());
        pessoa.setIdade(criarPessoaDto.idade());
        pessoa.setLogin(criarPessoaDto.login());
        pessoa.setSenha(criarPessoaDto.senha());
        pessoa.setStatus(criarPessoaDto.status());

        // Buscar endereços existentes da pessoa
        var enderecosExistentes = pessoa.getEnderecos();

        // Separar os códigos dos endereços recebidos
        var codigosRecebidos = criarPessoaDto.enderecos().stream()
                .map(CadrastroEndereco::codigoEndereco)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Remover endereços que não estão mais na lista recebida
        enderecosExistentes.removeIf(endereco -> !codigosRecebidos.contains(endereco.getCodigoEndereco()));

        // Processar endereços da lista recebida
        for (CadrastroEndereco enderecoDto : criarPessoaDto.enderecos()) {
            if (enderecoDto.codigoEndereco() != null) {
                // Atualizar endereço existente
                var endereco = enderecosExistentes.stream()
                        .filter(e -> e.getCodigoEndereco().equals(enderecoDto.codigoEndereco()))
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

                endereco.setNomeRua(enderecoDto.nomeRua());
                endereco.setNumero(enderecoDto.numero());
                endereco.setComplemento(enderecoDto.complemento());
                endereco.setCep(enderecoDto.cep());

                var bairro = bairroRepositorio.findById(enderecoDto.codigoBairro())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bairro não encontrado"));
                endereco.setBairro(bairro);

            } else {
                // Adicionar novo endereço
                var bairro = bairroRepositorio.findById(enderecoDto.codigoBairro())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bairro não encontrado"));

                var novoEndereco = new Endereco(
                        null,
                        null,
                        Instant.now(),
                        enderecoDto.cep(),
                        enderecoDto.complemento(),
                        enderecoDto.nomeRua(),
                        enderecoDto.numero(),
                        bairro,
                        pessoa
                );
                pessoa.getEnderecos().add(novoEndereco);
            }
        }

        // Salvar as alterações
        pessoaRepositorio.save(pessoa);
    }

    @Transactional
    public void desativarPessoa(Long codigoPessoa) {
        var pessoa = pessoaRepositorio.findById(codigoPessoa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));

        // Atualiza o status para 2 (desativado)
        pessoa.setStatus(2);

        // Salva a atualização
        pessoaRepositorio.save(pessoa);
    }

}
