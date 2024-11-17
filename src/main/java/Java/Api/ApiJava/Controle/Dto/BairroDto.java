package Java.Api.ApiJava.Controle.Dto;

import Java.Api.ApiJava.entity.Bairro;

public record BairroDto(Long codigoBairro,
                        Long codigoMunicipio,
                        String nome,
                        Integer status
                        ) {
    public BairroDto(Bairro bairro) {
        this(
                bairro.getCodigoBairro(),
                bairro.getMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus()
        );
    }
}
