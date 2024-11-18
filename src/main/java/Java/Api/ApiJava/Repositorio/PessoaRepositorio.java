package Java.Api.ApiJava.Repositorio;

import Java.Api.ApiJava.entity.Pessoa;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository

public interface PessoaRepositorio extends JpaRepository<Pessoa, Long> {

    @EntityGraph(attributePaths = {"enderecos", "enderecos.bairro"})
    Optional<Pessoa> findById(Long codigoPessoa);

    List<Pessoa> findByStatus(Integer status);

    List<Pessoa> findByLogin(String login);

    List<Pessoa> findByStatusAndLogin(Integer status, String login);





}


