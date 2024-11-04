package Java.Api.ApiJava.Repositorio;

import Java.Api.ApiJava.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PessoaRepositorio extends JpaRepository<Pessoa, Long> {

}
