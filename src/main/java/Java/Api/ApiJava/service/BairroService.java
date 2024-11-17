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



}
