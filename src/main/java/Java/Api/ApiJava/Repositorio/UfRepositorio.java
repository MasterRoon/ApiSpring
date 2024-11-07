package Java.Api.ApiJava.Repositorio;

import Java.Api.ApiJava.entity.Pessoa;
import Java.Api.ApiJava.entity.Uf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UfRepositorio extends JpaRepository<Uf, Long> {

}
