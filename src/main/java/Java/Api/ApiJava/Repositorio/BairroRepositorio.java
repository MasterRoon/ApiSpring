package Java.Api.ApiJava.Repositorio;

import Java.Api.ApiJava.entity.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface BairroRepositorio extends JpaRepository<Bairro, Long> {

}
