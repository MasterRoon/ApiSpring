package Java.Api.ApiJava.Controle.Dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AtualizarUf(@JsonAlias({"codigoUf", "codigoUF"})Long codigoUf, String sigla, String nome, Integer status) {
}
