package Java.Api.ApiJava.service;

import Java.Api.ApiJava.Controle.Dto.AtualizarMunicipio;
import Java.Api.ApiJava.Controle.Dto.InserirMunicipio;
import Java.Api.ApiJava.Repositorio.MunicipioRepositorio;
import Java.Api.ApiJava.Repositorio.UfRepositorio;
import Java.Api.ApiJava.entity.Municipio;
import Java.Api.ApiJava.entity.Uf;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Service
public class MunicipioService {

    private final MunicipioRepositorio municipioRepositorio;

    private final UfRepositorio ufRepositorio;

    public MunicipioService(MunicipioRepositorio municipioRepositorio, UfRepositorio ufRepositorio) {
        this.municipioRepositorio = municipioRepositorio;
        this.ufRepositorio = ufRepositorio;
    }

    public Municipio criarMunicipio(InserirMunicipio inserirMunicipio) {
        var uf = ufRepositorio.findById(inserirMunicipio.codigoUf())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UF não encontrada"));

        Municipio municipio = new Municipio(
                null,
                inserirMunicipio.nome(),
                1,
                uf,
                new HashSet<>(),
                Instant.now(),
                null
        );

        return municipioRepositorio.save(municipio);
    }

    public void atualizarMunicipios(Long codigoMunicipio, AtualizarMunicipio atualizarMunicipio) {
        // Busca o município existente pelo ID
        Municipio municipio = municipioRepositorio.findById(codigoMunicipio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipio não encontrado"));

        // Busca a UF associada
        Uf uf = ufRepositorio.findById(atualizarMunicipio.codigoUf())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UF não encontrada"));

        // Atualiza os campos do município
        municipio.setUf(uf);
        municipio.setNome(atualizarMunicipio.nome());
        municipio.setStatus(atualizarMunicipio.status());

        // Salva o município atualizado
        municipioRepositorio.save(municipio);
    }

    public List<Municipio> buscarMunicipios(Long codigoMunicipio, Long codigoUf, String nome, Integer status) {
        Specification<Municipio> spec = Specification.where(null);

        if (codigoMunicipio != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("codigoMunicipio"), codigoMunicipio));
        }
        if (codigoUf != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("uf").get("codigoUf"), codigoUf));
        }
        if (nome != null && !nome.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.like(builder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }
        if (status != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("status"), status));
        }

        return municipioRepositorio.findAll(spec);
    }

}
