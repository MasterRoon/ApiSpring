package Java.Api.ApiJava.Repositorio;


import Java.Api.ApiJava.entity.Uf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public interface UfRepositorio extends JpaRepository<Uf, Long> {
    boolean existsBySigla(String sigla);
    boolean existsByNome(String nome);
    boolean existsByNomeAndCodigoUfNot(String nome, Long codigoUf);
    boolean existsBySiglaAndCodigoUfNot(String sigla, Long codigoUf);

    @Query("SELECT u FROM Uf u WHERE " +
            "(:codigoUf IS NULL OR u.codigoUf = :codigoUf) AND " +
            "(:sigla IS NULL OR u.sigla LIKE %:sigla%) AND " +
            "(:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:status IS NULL OR u.status = :status)")
    List<Uf> findByFilters(@Param("codigoUf") Long codigoUf,
                           @Param("sigla") String sigla,
                           @Param("nome") String nome,
                           @Param("status") Integer status);


}
