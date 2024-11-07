package Java.Api.ApiJava.Repositorio;

import Java.Api.ApiJava.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MunicipioRepositorio extends JpaRepository<Municipio, Long> {

}
