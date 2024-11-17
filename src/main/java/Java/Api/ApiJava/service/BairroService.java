package Java.Api.ApiJava.service;

import jakarta.persistence.criteria.Predicate;
import Java.Api.ApiJava.Controle.Dto.*;
import Java.Api.ApiJava.Repositorio.BairroRepositorio;
import Java.Api.ApiJava.Repositorio.MunicipioRepositorio;
import Java.Api.ApiJava.entity.Bairro;
import Java.Api.ApiJava.entity.Municipio;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
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

    @Transactional
    public List<BairroDto> inserirBairro(InserirBairro dto) {
        // Valida se o código do município é válido
        Municipio municipio = municipioRepositorio.findById(dto.codigoMunicipio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Município não encontrado."));

        // Verifica se já existe um bairro com o mesmo nome no município
        boolean nomeExistente = bairroRepositorio.existsByNomeAndMunicipioCodigoMunicipio(dto.nome(), dto.codigoMunicipio());
        if (nomeExistente) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um bairro com este nome no município.");
        }

        // Cria a entidade Bairro
        Bairro bairro = new Bairro(
                null,
                dto.nome(),
                dto.status(),
                municipio,
                new HashSet<>(),
                Instant.now(),
                null
        );

        // Salva no banco
        bairroRepositorio.save(bairro);

        // Retorna todos os registros da tabela Bairro
        return bairroRepositorio.findAll()
                .stream()
                .map(BairroDto::new)
                .toList();
    }

    public List<BairroDto> buscarBairros(Long codigoBairro, Long codigoMunicipio, String nome, Integer status) {
        // Realiza a busca com base nos critérios
        List<Bairro> bairros = bairroRepositorio.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (codigoBairro != null) {
                predicates.add(criteriaBuilder.equal(root.get("codigoBairro"), codigoBairro));
            }

            if (codigoMunicipio != null) {
                predicates.add(criteriaBuilder.equal(root.get("municipio").get("codigoMunicipio"), codigoMunicipio));
            }

            if (nome != null && !nome.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        // Converte para DTO
        return bairros.stream().map(BairroDto::new).toList();
    }

    @Transactional
    public List<BairroDto> atualizarBairro(AtualizarBairro dto) {

        MunicipioService.ValidacaoUtil.validarCampoObrigatorio(dto.nome(), "O campo 'nome' é obrigatório e não pode estar vazio.");

        // Verifica se o código do bairro foi fornecido
        if (dto.codigoBairro() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O código do bairro é obrigatório para atualização.");
        }

        // Busca o bairro no banco de dados
        Bairro bairro = bairroRepositorio.findById(dto.codigoBairro())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bairro não encontrado."));

        // Valida se o nome já existe em outro bairro
        if (bairroRepositorio.existsByNomeAndCodigoBairroNot(dto.nome(), dto.codigoBairro())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um bairro com este nome.");
        }

        // Valida se o código do município é válido
        Municipio municipio = municipioRepositorio.findById(dto.codigoMunicipio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Município não encontrado."));

        // Atualiza os campos do bairro
        bairro.setNome(dto.nome());
        bairro.setStatus(dto.status());
        bairro.setMunicipio(municipio);
        bairro.setUpdateTimestamp(Instant.now());

        bairroRepositorio.save(bairro);

        return bairroRepositorio.findAll().stream().map(BairroDto::new).toList();
    }

    public static class ValidacaoUtil {
        public static void validarCampoObrigatorio(String campo, String mensagem) {
            if (campo == null || campo.trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagem);
            }
        }
    }

    @Transactional
    public List<BairroDto> deletarBairro(Long codigoBairro) {
        // Verifica se a Municipio existe pelo código
        Bairro bairro = bairroRepositorio.findById(codigoBairro)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bairro não encontrado."));

        if(bairro.getStatus()==2){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O Bairro já está desativado");
        }

        // Atualiza o status para 2 (inativo)
        bairro.setStatus(2);
        bairro.setUpdateTimestamp(Instant.now());

        bairroRepositorio.save(bairro);

        // Retorna todos os registros da tabela Municipio
        return bairroRepositorio.findAll()
                .stream()
                .map(BairroDto::new)
                .toList();
    }

}
