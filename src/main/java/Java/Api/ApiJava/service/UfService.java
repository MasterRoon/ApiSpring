package Java.Api.ApiJava.service;

import Java.Api.ApiJava.Controle.Dto.AtualizarUf;
import Java.Api.ApiJava.Controle.Dto.InserirUf;
import Java.Api.ApiJava.Controle.Dto.UfDto;
import Java.Api.ApiJava.Repositorio.UfRepositorio;
import Java.Api.ApiJava.entity.Uf;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UfService {

    @Autowired
    private UfRepositorio ufRepositorio;


    public List<UfDto> inserirUf(InserirUf inserirUf) {

        boolean siglaExistente = ufRepositorio.existsBySigla(inserirUf.sigla());
        boolean nomeExistente = ufRepositorio.existsByNome(inserirUf.nome());

        if (siglaExistente || nomeExistente) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Já existe uma UF com a mesma sigla ou nome no banco de dados.");
        }
        var entity = new Uf(
                null,
                Instant.now(),
                1,
                inserirUf.sigla(),
                inserirUf.nome(),
                new HashSet<>(),
                Instant.now()
        );
        var UfSalva = ufRepositorio.save(entity);
        return ufRepositorio.findAll().stream()
                .map(UfDto::new)
                .collect(Collectors.toList());
    }

    public List<Uf> listarTodasUfs() {
        var ListaUf = ufRepositorio.findAll();
        return ListaUf;
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
