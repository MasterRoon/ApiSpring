package Java.Api.ApiJava.Controle.Dto;


import com.fasterxml.jackson.annotation.JsonAlias;

public record InserirMunicipio(@JsonAlias({"codigoUf", "codigoUF"})Long codigoUf, String nome, Integer status) {
}
