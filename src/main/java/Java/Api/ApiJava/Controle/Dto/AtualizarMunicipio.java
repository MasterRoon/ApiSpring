package Java.Api.ApiJava.Controle.Dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AtualizarMunicipio(Long codigoMunicipio,
                                 @JsonAlias({"codigoUf", "codigoUF"})Long codigoUf,
                                 String nome,
                                 Integer status) {
}
