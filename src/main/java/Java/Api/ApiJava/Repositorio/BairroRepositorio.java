package Java.Api.ApiJava.Repositorio;

import Java.Api.ApiJava.entity.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository

public interface BairroRepositorio extends JpaRepository<Bairro, Long>, JpaSpecificationExecutor<Bairro> {
    boolean existsByNomeAndMunicipioCodigoMunicipio(String nome, Long codigoMunicipio);
    boolean existsByNomeAndCodigoBairroNot(String nome, Long codigoBairro);


}
