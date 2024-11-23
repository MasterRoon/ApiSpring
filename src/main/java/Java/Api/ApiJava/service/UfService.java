package Java.Api.ApiJava.service;

import Java.Api.ApiJava.Controle.Dto.AtualizarUf;
import Java.Api.ApiJava.Controle.Dto.InserirUf;
import Java.Api.ApiJava.Controle.Dto.UfDto;
import Java.Api.ApiJava.Repositorio.UfRepositorio;
import Java.Api.ApiJava.entity.Uf;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UfService {


    private final UfRepositorio ufRepositorio;

    public UfService(UfRepositorio ufRepositorio) {
        this.ufRepositorio = ufRepositorio;
    }

    public List<UfDto> inserirUf(InserirUf inserirUf) {

        boolean siglaExistente = ufRepositorio.existsBySigla(inserirUf.sigla());
        boolean nomeExistente = ufRepositorio.existsByNome(inserirUf.nome());

        if(inserirUf.status() != 1 || inserirUf.status() != 2){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'status' é obrigatório ter valores de 1 ou 2 .");
        }

        if (inserirUf.sigla() == null || inserirUf.sigla().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'Sigla' é obrigatório e não pode estar vazio.");
        }

        if (inserirUf.nome() == null || inserirUf.nome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'Nome' é obrigatório.");
        }

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
        ufRepositorio.save(entity);
        return ufRepositorio.findAll().stream()
                .map(UfDto::new)
                .collect(Collectors.toList());
    }

    public List<UfDto> buscarUfs(Long codigoUf, String sigla, String nome, Integer status) {
        List<Uf> ufs;

        if (codigoUf != null) {
            // Filtrar por código UF
            ufs = ufRepositorio.findByCodigoUf(codigoUf)
                    .map(List::of)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UF não encontrada."));

        } else if (sigla != null) {
            // Filtrar por sigla
            ufs = ufRepositorio.findBySigla(sigla);

        } else if (nome != null) {
            // Filtrar por nome (parcial, sem case-sensitive)
            ufs = ufRepositorio.findByNomeContainingIgnoreCase(nome);

        } else if (status != null) {
            // Filtrar por status
            ufs = ufRepositorio.findByStatus(status);

        } else {
            // Retornar todas as UFs se nenhum filtro for fornecido
            ufs = ufRepositorio.findAll();
        }

        // Converter para DTO
        return ufs.stream()
                .map(UfDto::new)
                .collect(Collectors.toList());
    }

    public List<UfDto> atualizarUf(AtualizarUf dto) {
        // Valida se o código da UF foi fornecido
        if (dto.codigoUf() == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O código da UF é obrigatório para a atualização.");
        }

        if (dto.sigla() == null|| dto.sigla().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Sigla é obrigatório para a atualização.");
        }

        if (dto.nome() == null || dto.nome().trim().isEmpty() ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Nome é obrigatório para a atualização.");
        }

        if (dto.status() == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Status é obrigatório para a atualização.");
        }

        // Busca a UF pelo código
        Uf uf = ufRepositorio.findById(dto.codigoUf())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UF não encontrada."));

        // Valida se o novo nome já existe para outra UF
        if (ufRepositorio.existsByNomeAndCodigoUfNot(dto.nome(), dto.codigoUf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma UF com este nome.");
        }

        // Valida se a nova sigla já existe para outra UF
        if (ufRepositorio.existsBySiglaAndCodigoUfNot(dto.sigla(), dto.codigoUf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma UF com esta sigla.");
        }

        // Atualiza os campos permitidos
        uf.setNome(dto.nome());
        uf.setSigla(dto.sigla());
        uf.setStatus(dto.status());
        uf.setUpdateTimestamp(Instant.now());

        ufRepositorio.save(uf);

        // Retorna a lista de todas as UFs
        return ufRepositorio.findAll()
                .stream()
                .map(UfDto::new)
                .toList();
    }

    @Transactional
    public List<UfDto> deletarUf(Long codigoUf) {
        // Verifica se a UF existe pelo código
        Uf uf = ufRepositorio.findById(codigoUf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UF não encontrada."));

        if (uf.getStatus() == 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O código do Municipio já está desativado.");
        }

        // Atualiza o status para 2 (inativo)
        uf.setStatus(2);
        uf.setUpdateTimestamp(Instant.now());

        ufRepositorio.save(uf);

        // Retorna todos os registros da tabela UF
        return ufRepositorio.findAll()
                .stream()
                .map(UfDto::new)
                .toList();
    }


}
