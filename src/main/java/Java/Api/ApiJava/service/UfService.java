package Java.Api.ApiJava.service;

import Java.Api.ApiJava.Controle.Dto.AtualizarUf;
import Java.Api.ApiJava.Controle.Dto.InserirUf;
import Java.Api.ApiJava.Repositorio.UfRepositorio;
import Java.Api.ApiJava.entity.Uf;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UfService {

    private UfRepositorio ufRepositorio;


    public long inserirUf(InserirUf inserirUf) {
        var entity = new Uf(null,                  // código da UF
                Instant.now(),                         // updateTimestamp
                1,                                     // status (valor padrão, ex: ativo)
                inserirUf.sigla(),                     // sigla da UF
                inserirUf.nome(),                      // nome da UF
                new HashSet<>(),                       // municípios (inicialmente vazio)
                Instant.now());
        var UfSalva = ufRepositorio.save(entity);

        return UfSalva.getCodigoUf();

    }

    public Optional<Uf> getUfByCodigoUf(String codigoUf) {
        return ufRepositorio.findById(Long.valueOf(codigoUf));
    }

    public List<Uf> ListarUF() {
        return ufRepositorio.findAll();
    }

    public void atualizarPessoaId(String codigoUf, AtualizarUf atualizarUf) {
        var id = Long.valueOf(codigoUf);
        var ufExiste = ufRepositorio.findById(id);

        if(ufExiste.isPresent()){
            var atualizacao = ufExiste.get();

            if(atualizarUf.sigla()!=null){
                atualizacao.setSigla(atualizarUf.sigla());
            }
            if(atualizarUf.nome()!=null){
                atualizacao.setNome(atualizarUf.nome());
            }

            ufRepositorio.save(atualizacao);
        }
    }

    public void DesativarMunicipioByCodigoMunicipio(Long codigoUf) {
        Optional<Uf> ufOptional = ufRepositorio.findById(codigoUf);
        if (ufOptional.isPresent()) {
            Uf uf = ufOptional.get();
            uf.setStatus(2);  // Define o status como 2 para "desativado"
            ufRepositorio.save(uf);
        } else {
            throw new EntityNotFoundException("Pessoa com código " + codigoUf + " não encontrada.");
        }
    }

}
