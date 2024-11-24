package Java.Api.ApiJava.Controle.Dto.ImpressaoPessoa;

public record BairroRespostaCompletaDto(Long codigoBairro,
                                        Long codigoMunicipio,
                                        String nome,
                                        Integer status,
                                        MunicipioRespostaCompletaDto municipio) {
}
