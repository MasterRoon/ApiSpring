package Java.Api.ApiJava.Controle.Dto.ImpressãoPessoa;

public record MunicipioRespostaCompletaDto( Long codigoMunicipio,
                                            Long codigoUf,
                                            String nome,
                                            Integer status,
                                            UfRespostaDto uf) {
}
