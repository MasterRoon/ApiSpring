package Java.Api.ApiJava.Controle.Dto;

import java.util.List;

public record CadrastroEndereco(Long codigoEndereco,
                                String nomeRua,
                                Integer numero,
                                String complemento,
                                String cep,
                                Long codigoBairro) {
}
