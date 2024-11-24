package Java.Api.ApiJava.Controle.Dto.ImpressaoPessoa;

public record MunicipioRespostaCompletaDto( Long codigoMunicipio,
                                            Long codigoUf,
                                            String nome,
                                            Integer status,
                                            UfRespostaDto uf) {
}
