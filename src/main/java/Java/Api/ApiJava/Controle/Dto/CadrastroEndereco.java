package Java.Api.ApiJava.Controle.Dto;

import java.util.List;

public record CadrastroEndereco(Long codigoEndereco,
                                Long codigoBairro,
                                String nomeRua,
                                Integer numero,
                                String complemento,
                                String cep) {
}
