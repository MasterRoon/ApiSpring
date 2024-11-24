package Java.Api.ApiJava.Controle.Dto.ImpressaoPessoa;

public record EnderecoRespostaCompletaDto(Long codigoEndereco,
                                          Long codigoPessoa,
                                          Long codigoBairro,
                                          String nomeRua,
                                          String numero,
                                          String complemento,
                                          String cep,
                                          BairroRespostaCompletaDto bairro) {
}
