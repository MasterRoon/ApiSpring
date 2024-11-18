package Java.Api.ApiJava.Controle.Dto;

import java.util.List;

public record CadrastroEndereco(Long codigoBairro,
                                Long codigoEndereco,
                                String nomeRua,
                                String numero,
                                String complemento,
                                String cep) {
}
