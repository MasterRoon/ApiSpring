package Java.Api.ApiJava.service;

import Java.Api.ApiJava.Controle.Dto.AtualizarDto;
import Java.Api.ApiJava.Controle.Dto.CadrastroEndereco;
import Java.Api.ApiJava.Controle.Dto.CriarPessoaDto;
import Java.Api.ApiJava.Repositorio.EnderecoRepositorio;
import Java.Api.ApiJava.Repositorio.PessoaRepositorio;
import Java.Api.ApiJava.entity.Bairro;
import Java.Api.ApiJava.entity.Endereco;
import Java.Api.ApiJava.entity.Pessoa;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    private PessoaRepositorio pessoaRepositorio;
    
    private EnderecoRepositorio enderecoRepositorio;

    public PessoaService(PessoaRepositorio pessoaRepositorio, EnderecoRepositorio enderecoRepositorio) {
        this.pessoaRepositorio = pessoaRepositorio;
        this.enderecoRepositorio = enderecoRepositorio;
    }

    public long createPessoa(CriarPessoaDto criarPessoaDto) {
        var entity = new Pessoa(
                criarPessoaDto.codigoPessoa(),//id da pessoa
                null,
                Instant.now(),               // creationTimestamp
                new HashSet<>(),             // enderecos (passando um conjunto vazio, por exemplo)
                1,                           // status (ou outro valor padrão)
                criarPessoaDto.nome(),       // nome
                criarPessoaDto.sobrenome(),  // sobrenome
                criarPessoaDto.idade(),      // idade
                criarPessoaDto.login(),
                criarPessoaDto.senha()

                //null,
                //criarPessoaDto.nome(),       //
                //criarPessoaDto.sobrenome(),  //
                //criarPessoaDto.idade(),      //
                //criarPessoaDto.login(),      //
                //criarPessoaDto.senha()      // versão antiga mas o null não me deixa resolver de jeito nenhum
                //Instant.now(),              //
                //null                        //

         );

        var UsuarioSavo = pessoaRepositorio.save(entity);
        return UsuarioSavo.getCodigoPessoa();
    }

    public Optional<Pessoa> getPessoaByCodigoPessoa(String codigoPessoa) {
        return pessoaRepositorio.findById(Long.valueOf(codigoPessoa));
    }

    public List<Pessoa> ListarPessoas() {
        return pessoaRepositorio.findAll();
    }

    public void atualizarPessoaId(String codigoPessoa, AtualizarDto atualizarDto) {
        var id = Long.valueOf(codigoPessoa);
        var pessoaExiste = pessoaRepositorio.findById(id);

        if(pessoaExiste.isPresent()){
            var atualizacao = pessoaExiste.get();

            if(atualizarDto.nome()!=null){
                atualizacao.setNome(atualizarDto.nome());
            }
            if(atualizarDto.sobrenome()!=null){
                atualizacao.setSobrenome(atualizarDto.sobrenome());
            }
            if(atualizarDto.idade()!=null){
                atualizacao.setIdade(atualizarDto.idade());
            }
            if(atualizarDto.senha()!=null){
                atualizacao.setSenha(atualizarDto.senha());
            }
            pessoaRepositorio.save(atualizacao);
        }
    }

    public void DeletePessoaByCodigoPessoa(Long codigoPessoa) {
        Optional<Pessoa> pessoaOptional = pessoaRepositorio.findById(codigoPessoa);
        if (pessoaOptional.isPresent()) {
            Pessoa pessoa = pessoaOptional.get();
            pessoa.setStatus(2);  // Define o status como 2 para "desativado"
            pessoaRepositorio.save(pessoa);
        } else {
            throw new EntityNotFoundException("Pessoa com código " + codigoPessoa + " não encontrada.");
        }
    }


    public void cadastroEndereco(String codigoPessoa, CadrastroEndereco cadrastroEndereco) {
        
        var pessoa = pessoaRepositorio.findById(Long.parseLong(codigoPessoa))
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var endereco = new Endereco(
                cadrastroEndereco.codigoEndereco(),
                null,
                Instant.now(),
                cadrastroEndereco.nomeRua(),          // nome da rua
                cadrastroEndereco.numero(),           // número
                cadrastroEndereco.complemento(),      // complemento (pode ser opcional)
                cadrastroEndereco.cep(),            // cep
                pessoa,
                Bairro
            );
        var enderecoCadrastrado = enderecoRepositorio.save(endereco);
    }
}
