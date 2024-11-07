package Java.Api.ApiJava.Repositorio;

import Java.Api.ApiJava.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EnderecoRepositorio extends JpaRepository<Endereco, Long> {

}
