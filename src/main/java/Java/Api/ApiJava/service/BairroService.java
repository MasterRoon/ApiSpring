package Java.Api.ApiJava.service;


import Java.Api.ApiJava.Controle.Dto.AtualizarBairro;
import Java.Api.ApiJava.Controle.Dto.AtualizarMunicipio;
import Java.Api.ApiJava.Controle.Dto.InserirBairro;
import Java.Api.ApiJava.Controle.Dto.InserirMunicipio;
import Java.Api.ApiJava.Repositorio.BairroRepositorio;
import Java.Api.ApiJava.Repositorio.MunicipioRepositorio;
import Java.Api.ApiJava.entity.Bairro;
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
public class BairroService {

    public final BairroRepositorio bairroRepositorio;

    public final MunicipioRepositorio municipioRepositorio;

    public BairroService(BairroRepositorio bairroRepositorio, MunicipioRepositorio municipioRepositorio) {
        this.bairroRepositorio = bairroRepositorio;
        this.municipioRepositorio = municipioRepositorio;
    }

    public Bairro criarBairro(InserirBairro inserirBairro) {
        var municipio = municipioRepositorio.findById(inserirBairro.codigoMunicipio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipio não encontrado"));

        Bairro bairro = new Bairro(
                null,
                inserirBairro.nome(),
                1,
                municipio,
                new HashSet<>(),
                Instant.now(),
                null
        );

        return bairroRepositorio.save(bairro);
    }

    public void atualizarBairros(Long codigoBairro, AtualizarBairro atualizarBairro) {
        // Busca o bairro existente pelo ID
        Bairro bairro = bairroRepositorio.findById(codigoBairro)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "bairro não encontrado"));

        // Busca o Municipio associada
        Municipio municipio = municipioRepositorio.findById(atualizarBairro.codigoMunicipio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipio não encontrado"));

        // Atualiza os campos do bairro
        bairro.setMunicipio(municipio);
        bairro.setNome(atualizarBairro.nome());
        municipio.setStatus(atualizarBairro.status());

        // Salva o bairro atualizado
        bairroRepositorio.save(bairro);
    }

    public List<Bairro> buscarBairros(Long codigoBairro, Long codigoMunicipio, String nome, Integer status) {
        Specification<Bairro> spec = Specification.where(null);

        if (codigoBairro != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("codigoMunicipio"), codigoBairro));
        }
        if (codigoMunicipio != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("uf").get("codigoUf"), codigoMunicipio));
        }
        if (nome != null && !nome.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.like(builder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }
        if (status != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("status"), status));
        }

        return bairroRepositorio.findAll(spec);
    }

    public void desativarBairro(Long codigoBairro) {
        var bairro = bairroRepositorio.findById(codigoBairro)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bairro não encontrado"));

        // Atualizar status para 2, indicando desativado
        bairro.setStatus(2);
        bairroRepositorio.save(bairro);
    }

}
