package Java.Api.ApiJava.Controle.Dto;

import java.util.List;

public record PessoaRespostaDto(Long codigoPessoa,
                                String nome,
                                String sobrenome,
                                Integer idade,
                                String login,
                                String senha,
                                Integer status,
                                List<EnderecoRespostaDto> enderecos) {
}

