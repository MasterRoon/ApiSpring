package Java.Api.ApiJava.Repositorio;


import Java.Api.ApiJava.entity.Uf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UfRepositorio extends JpaRepository<Uf, Long> {
    boolean existsBySigla(String sigla);
    boolean existsByNome(String nome);

    Optional<Uf> findByCodigoUf(Long codigoUf);

    List<Uf> findBySigla(String sigla);

    List<Uf> findByNomeContainingIgnoreCase(String nome);

    List<Uf> findByStatus(Integer status);

    boolean existsByNomeAndCodigoUfNot(String nome, Long codigoUf);
    boolean existsBySiglaAndCodigoUfNot(String sigla, Long codigoUf);

}
