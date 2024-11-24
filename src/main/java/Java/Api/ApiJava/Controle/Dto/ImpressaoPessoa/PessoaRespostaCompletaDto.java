package Java.Api.ApiJava.Controle.Dto.ImpressaoPessoa;

import java.util.List;

public record PessoaRespostaCompletaDto(Long codigoPessoa,
                                        String nome,
                                        String sobrenome,
                                        Integer idade,
                                        String login,
                                        String senha,
                                        Integer status,
                                        List<EnderecoRespostaCompletaDto> enderecos) {
}
