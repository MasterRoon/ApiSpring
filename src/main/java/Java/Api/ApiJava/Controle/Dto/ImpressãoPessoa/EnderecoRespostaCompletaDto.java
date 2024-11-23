package Java.Api.ApiJava.Controle.Dto.ImpressãoPessoa;

public record EnderecoRespostaCompletaDto(Long codigoEndereco,
                                          Long codigoPessoa,
                                          Long codigoBairro,
                                          String nomeRua,
                                          String numero,
                                          String complemento,
                                          String cep,
                                          BairroRespostaCompletaDto bairro) {
}
