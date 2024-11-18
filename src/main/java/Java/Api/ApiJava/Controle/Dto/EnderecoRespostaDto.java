package Java.Api.ApiJava.Controle.Dto;

public record EnderecoRespostaDto(
        Long codigoEndereco,
        Long codigoBairro,
        String nomeRua,
        String numero,
        String complemento,
        String cep
) {}
