package Java.Api.ApiJava.service;


import Java.Api.ApiJava.Controle.Dto.AtualizarMunicipio;
import Java.Api.ApiJava.Controle.Dto.InserirMunicipio;
import Java.Api.ApiJava.Controle.Dto.MunicipioDto;
import Java.Api.ApiJava.Repositorio.MunicipioRepositorio;
import Java.Api.ApiJava.Repositorio.UfRepositorio;
import Java.Api.ApiJava.entity.Municipio;
import Java.Api.ApiJava.entity.Uf;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public List<MunicipioDto> criarMunicipio(InserirMunicipio dto) {
        // Verifica se já existe um município com o mesmo nome
        if (municipioRepositorio.existsByNome(dto.nome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um município com este nome.");
        }

        // Busca a UF associada
        Uf uf = ufRepositorio.findById(dto.codigoUf())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UF não encontrada."));

        // Cria o município
        Municipio municipio = new Municipio(
                null,
                dto.nome(),
                dto.status(),
                uf,
                new HashSet<>(),
                Instant.now(),
                Instant.now()
        );

        // Salva o município no banco de dados
        municipioRepositorio.save(municipio);

        // Retorna todos os registros da tabela Município
        return municipioRepositorio.findAll()
                .stream()
                .map(MunicipioDto::new)
                .toList();
    }

    public List<MunicipioDto> buscarMunicipios(Long codigoMunicipio, Long codigoUf, String nome, Integer status) {
        var municipios = municipioRepositorio.findByFilters(codigoMunicipio, codigoUf, nome, status);
        return municipios.stream().map(MunicipioDto::new).toList();
    }

    @Transactional
    public List<MunicipioDto> atualizarMunicipio(AtualizarMunicipio dto) {

        ValidacaoUtil.validarCampoObrigatorio(dto.nome(), "O campo 'nome' é obrigatório e não pode estar vazio.");


        // Valida se o código do município foi fornecido
        if (dto.codigoMunicipio() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O código do município é obrigatório para a atualização.");
        }

        // Busca o município pelo código
        Municipio municipio = municipioRepositorio.findById(dto.codigoMunicipio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Município não encontrado."));

        // Valida se o nome já existe em outro município
        if (municipioRepositorio.existsByNomeAndCodigoMunicipioNot(dto.nome(), dto.codigoMunicipio())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um município com este nome.");
        }

        // Valida se o código da UF é válido
        Uf uf = ufRepositorio.findById(dto.codigoUf())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UF não encontrada."));

        // Atualiza os campos
        municipio.setUf(uf);
        municipio.setNome(dto.nome());
        municipio.setStatus(dto.status());
        municipio.setUpdateTimestamp(Instant.now());

        // Salva as alterações
        municipioRepositorio.save(municipio);

        // Retorna todos os municípios atualizados
        return municipioRepositorio.findAll().stream().map(MunicipioDto::new).toList();
    }

    public static class ValidacaoUtil {
        public static void validarCampoObrigatorio(String campo, String mensagem) {
            if (campo == null || campo.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
            }
        }
    }


    @Transactional
    public List<MunicipioDto> deletarMunicipio(Long codigoMunicipio) {
        // Verifica se a Municipio existe pelo código
        Municipio municipio = municipioRepositorio.findById(codigoMunicipio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipio não encontrado."));

        // Atualiza o status para 2 (inativo)
        municipio.setStatus(2);
        municipio.setUpdateTimestamp(Instant.now());

        municipioRepositorio.save(municipio);

        // Retorna todos os registros da tabela Municipio
        return municipioRepositorio.findAll()
                .stream()
                .map(MunicipioDto::new)
                .toList();
    }

}
