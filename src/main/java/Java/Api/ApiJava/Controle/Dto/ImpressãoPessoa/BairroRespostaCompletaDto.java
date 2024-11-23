package Java.Api.ApiJava.Controle.Dto.ImpressãoPessoa;

public record BairroRespostaCompletaDto(Long codigoBairro,
                                        Long codigoMunicipio,
                                        String nome,
                                        Integer status,
                                        MunicipioRespostaCompletaDto municipio) {
}
