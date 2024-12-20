package Java.Api.ApiJava.service;



import Java.Api.ApiJava.Controle.Dto.AtualizarPessoaDto;
import Java.Api.ApiJava.Controle.Dto.CadrastroEndereco;
import Java.Api.ApiJava.Controle.Dto.CriarPessoaDto;
import Java.Api.ApiJava.Controle.Dto.ImpressaoPessoa.*;
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

        validarPessoaDto(pessoaDto);


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

            validarEndereco(enderecoDto);

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

    @Transactional
    public PessoaRespostaCompletaDto buscarPessoaCompleta(Long codigoPessoa) {
        return pessoaRepositorio.findById(codigoPessoa)
                .map(pessoa -> new PessoaRespostaCompletaDto(
                        pessoa.getCodigoPessoa(),
                        pessoa.getNome(),
                        pessoa.getSobrenome(),
                        pessoa.getIdade(),
                        pessoa.getLogin(),
                        pessoa.getSenha(),
                        pessoa.getStatus(),
                        pessoa.getEnderecos().stream().map(endereco -> new EnderecoRespostaCompletaDto(
                                endereco.getCodigoEndereco(),
                                endereco.getPessoa().getCodigoPessoa(),
                                endereco.getBairro().getCodigoBairro(),
                                endereco.getNomeRua(),
                                String.valueOf(endereco.getNumero()), // Conversão para String
                                endereco.getComplemento(),
                                endereco.getCep(),
                                new BairroRespostaCompletaDto(
                                        endereco.getBairro().getCodigoBairro(),
                                        endereco.getBairro().getMunicipio().getCodigoMunicipio(),
                                        endereco.getBairro().getNome(),
                                        endereco.getBairro().getStatus(),
                                        new MunicipioRespostaCompletaDto(
                                                endereco.getBairro().getMunicipio().getCodigoMunicipio(),
                                                endereco.getBairro().getMunicipio().getUf().getCodigoUf(),
                                                endereco.getBairro().getMunicipio().getNome(),
                                                endereco.getBairro().getMunicipio().getStatus(),
                                                new UfRespostaDto(
                                                        endereco.getBairro().getMunicipio().getUf().getCodigoUf(),
                                                        endereco.getBairro().getMunicipio().getUf().getSigla(),
                                                        endereco.getBairro().getMunicipio().getUf().getNome(),
                                                        endereco.getBairro().getMunicipio().getUf().getStatus()
                                                )
                                        )
                                )
                        )).toList()
                ))
                // Se a pessoa não for encontrada, retorna um DTO vazio com endereços como uma lista vazia
                .orElse(null);
    }

    @Transactional
    public Pessoa atualizarPessoaComEnderecos(AtualizarPessoaDto pessoaDto) {


        if (pessoaDto.status() == null || (pessoaDto.status() != 1 && pessoaDto.status() != 2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'status' deve ser 1 (ativo) ou 2 (desativado).");
        }

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

    public List<Pessoa> buscarTodasPessoas() {
        return pessoaRepositorio.findAll();
    }

    @Transactional
    public void desativarPessoa(Long codigoPessoa) {
        Pessoa pessoa = pessoaRepositorio.findById(codigoPessoa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));

        if (codigoPessoa <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O código da pessoa deve ser um número positivo.");
        }

        if (pessoa.getStatus() == 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A pessoa já está desativada.");
        }

        pessoa.setStatus(2);
        pessoaRepositorio.save(pessoa);

    }

    public void validarPessoaDto(CriarPessoaDto pessoaDto) {
        // Validação do nome
        if (pessoaDto.nome() == null || pessoaDto.nome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'nome' é obrigatório e não pode estar vazio.");
        }

        // Validação do sobrenome
        if (pessoaDto.sobrenome() == null || pessoaDto.sobrenome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'sobrenome' é obrigatório e não pode estar vazio.");
        }

        // Validação da idade
        if (pessoaDto.idade() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'idade' é obrigatório.");
        }

        if (pessoaDto.idade() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'idade' deve ser maior que zero.");
        }

        // Validação do login
        if (pessoaDto.login() == null || pessoaDto.login().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'login' é obrigatório e não pode estar vazio.");
        }

        if (pessoaRepositorio.existsByLogin(pessoaDto.login())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O login informado já está em uso.");
        }

        // Validação da senha
        if (pessoaDto.senha() == null || pessoaDto.senha().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'senha' é obrigatório e não pode estar vazio.");
        }

        if (pessoaDto.senha().length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'senha' deve ter pelo menos 5 caracteres.");
        }

        if (!pessoaDto.login().matches("^[a-zA-Z0-9._-]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'login' contém caracteres inválidos. Apenas letras, números, '.', '-' e '_' são permitidos.");
        }

        // Validação do endereço
        if (pessoaDto.enderecos() == null || pessoaDto.enderecos().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "É obrigatório cadastrar ao menos um endereço.");
        }
    }

    public void validarEndereco(CadrastroEndereco enderecoDto){
        if (enderecoDto.nomeRua() == null || enderecoDto.nomeRua().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'nomeRua' é obrigatório e não pode estar vazio.");
        }

        if (enderecoDto.numero() == null || !enderecoDto.numero().matches("\\d+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'numero' é obrigatório e deve conter apenas números.");
        }

        if (enderecoDto.cep() == null || !enderecoDto.cep().matches("\\d{5}-\\d{3}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'cep' é obrigatório e deve seguir o formato XXXXX-XXX.");
        }
    }

}
