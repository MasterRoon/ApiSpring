package Java.Api.ApiJava.Repositorio;

import Java.Api.ApiJava.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@Repository

public interface PessoaRepositorio extends JpaRepository<Pessoa, Long> {

    Optional<Pessoa> findById(Long codigoPessoa);

    List<Pessoa> findByLogin(String login);

    List<Pessoa> findByStatus(Integer status);

    // Método personalizado para buscar todas as pessoas sem os endereços
    @Query("SELECT p FROM Pessoa p LEFT JOIN FETCH p.enderecos e WHERE e IS NULL")
    List<Pessoa> findAllWithoutEnderecos();
}


