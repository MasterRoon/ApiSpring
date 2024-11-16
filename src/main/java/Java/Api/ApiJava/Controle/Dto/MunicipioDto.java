package Java.Api.ApiJava.Controle.Dto;

import Java.Api.ApiJava.entity.Municipio;

public record MunicipioDto(Long codigoMunicipio,
                           Long codigoUf,
                           String nome,
                           Integer status

) {
    public MunicipioDto(Municipio municipio) {
        this(
                municipio.getCodigoMunicipio(),
                municipio.getUf().getCodigoUf(),
                municipio.getNome(),
                municipio.getStatus()

        );
    }
}

