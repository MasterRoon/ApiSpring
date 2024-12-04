package Java.Api.ApiJava.Controle.Dto;

import Java.Api.ApiJava.entity.Uf;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UfDto(
        @JsonProperty("codigoUF")Long codigoUf,
        String sigla,
        String nome,
        Integer status,
        Instant updateTimestamp
) {


    public UfDto(Uf uf) {
        this(uf.getCodigoUf(), uf.getSigla(), uf.getNome(), uf.getStatus(), uf.getUpdateTimestamp());
    }
}
