package Java.Api.ApiJava.service;



import Java.Api.ApiJava.Controle.Dto.AtualizarPessoaDto;
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


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PessoaService {

    private final PessoaRepositorio pessoaRepositorio;
    private final BairroRepositorio bairroRepositorio;
    private final EnderecoRepositorio enderecoRepositorio;

    @Autowired
    public PessoaService(PessoaRepositorio pessoaRepositorio,EnderecoRepositorio enderecoRepositorio, BairroRepositorio bairroRepositorio) {
        this.pessoaRepositorio = pessoaRepositorio;
        this.enderecoRepositorio = enderecoRepositorio;
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

    public List<Pessoa> buscarPorStatusOuLogin(Integer status, String login) {
        if (status != null && login != null) {
            return pessoaRepositorio.findByStatusAndLogin(status, login);
        } else if (status != null) {
            return pessoaRepositorio.findByStatus(status);
        } else if (login != null) {
            return pessoaRepositorio.findByLogin(login);
        } else {
            return pessoaRepositorio.findAll();
        }
    }

    public Pessoa buscarPorCodigoPessoa(Long codigoPessoa) {
        return pessoaRepositorio.findById(codigoPessoa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
    }

    @Transactional
    public Pessoa atualizarPessoaComEnderecos(AtualizarPessoaDto pessoaDto) {

        // Busca a pessoa existente
        Pessoa pessoa = pessoaRepositorio.findById(pessoaDto.codigoPessoa())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));

        // Atualiza os campos da Pessoa
        pessoa.setNome(pessoaDto.nome());
        pessoa.setSobrenome(pessoaDto.sobrenome());
        pessoa.setIdade(pessoaDto.idade());
        pessoa.setLogin(pessoaDto.login());
        pessoa.setSenha(pessoaDto.senha());
        pessoa.setStatus(pessoaDto.status());

        // Lista de códigos de endereços fornecidos no JSON
        List<Long> codigosEnderecosNoJson = new ArrayList<>();
        List<Endereco> novosEnderecos = new ArrayList<>();

        // Processar os endereços do JSON
        for (CadrastroEndereco enderecoDto : pessoaDto.enderecos()) {
            if (enderecoDto.codigoEndereco() != null) {
                // Atualizar endereço existente
                Endereco endereco = pessoa.getEnderecos().stream()
                        .filter(e -> e.getCodigoEndereco().equals(enderecoDto.codigoEndereco()))
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));

                Bairro bairro = bairroRepositorio.findById(enderecoDto.codigoBairro())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bairro não encontrado"));

                endereco.setBairro(bairro);
                endereco.setNomeRua(enderecoDto.nomeRua());
                endereco.setNumero(Integer.valueOf(enderecoDto.numero()));
                endereco.setComplemento(enderecoDto.complemento());
                endereco.setCep(enderecoDto.cep());

                codigosEnderecosNoJson.add(enderecoDto.codigoEndereco());
            } else {
                // Criar novo endereço
                Bairro bairro = bairroRepositorio.findById(enderecoDto.codigoBairro())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bairro não encontrado"));

                Endereco novoEndereco = new Endereco();
                novoEndereco.setPessoa(pessoa);
                novoEndereco.setBairro(bairro);
                novoEndereco.setNomeRua(enderecoDto.nomeRua());
                novoEndereco.setNumero(Integer.valueOf(enderecoDto.numero()));
                novoEndereco.setComplemento(enderecoDto.complemento());
                novoEndereco.setCep(enderecoDto.cep());

                novosEnderecos.add(novoEndereco);
            }
        }

        // Remover endereços antigos que não estão no JSON
        List<Endereco> enderecosParaRemover = pessoa.getEnderecos().stream()
                .filter(endereco -> !codigosEnderecosNoJson.contains(endereco.getCodigoEndereco()))
                .collect(Collectors.toList());

        // Remove os endereços da pessoa e também do banco de dados
        pessoa.getEnderecos().removeAll(enderecosParaRemover);
        enderecoRepositorio.deleteAll(enderecosParaRemover);

        // Adicionar novos endereços
        pessoa.getEnderecos().addAll(novosEnderecos);

        // Salvar a pessoa atualizada
        return pessoaRepositorio.save(pessoa);
    }

    @Transactional
    public void desativarPessoa(Long codigoPessoa) {
        Pessoa pessoa = pessoaRepositorio.findById(codigoPessoa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));

        if (pessoa.getStatus() == 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A pessoa já está desativada.");
        }

        pessoa.setStatus(2);
        pessoaRepositorio.save(pessoa);

    }

}
