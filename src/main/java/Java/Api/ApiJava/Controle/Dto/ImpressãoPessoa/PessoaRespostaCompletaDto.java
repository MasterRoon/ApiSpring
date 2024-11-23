package Java.Api.ApiJava.Controle.Dto.ImpressãoPessoa;

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
