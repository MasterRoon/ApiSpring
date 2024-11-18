package Java.Api.ApiJava.Controle.Dto;

import java.util.List;

public record CadrastroEndereco(Long codigoBairro,
                                String nomeRua,
                                String numero,
                                String complemento,
                                String cep) {
}
