package Java.Api.ApiJava.service;

import Java.Api.ApiJava.Controle.CriarPessoaDto;
import Java.Api.ApiJava.Repositorio.PessoaRepositorio;
import Java.Api.ApiJava.entity.Pessoa;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.HashSet;

@Service
public class PessoaService {

    private PessoaRepositorio pessoaRepositorio;

    public PessoaService(PessoaRepositorio pessoaRepositorio) {
        this.pessoaRepositorio = pessoaRepositorio;
    }

    public long createPessoa(CriarPessoaDto criarPessoaDto) {
        var entity = new Pessoa(
                null,
                null,
                Instant.now(),               // creationTimestamp
                new HashSet<>(),             // enderecos (passando um conjunto vazio, por exemplo) mas ta com problema quando da import
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

}
