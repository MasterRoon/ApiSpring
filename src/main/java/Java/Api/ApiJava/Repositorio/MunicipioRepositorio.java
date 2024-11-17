package Java.Api.ApiJava.Repositorio;

import Java.Api.ApiJava.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface MunicipioRepositorio extends JpaRepository<Municipio, Long>{
    boolean existsByNome(String nome);

    boolean existsByNomeAndCodigoMunicipioNot(String nome, Long codigoMunicipio);
        @Query("""
           SELECT m
           FROM Municipio m
           WHERE (:codigoMunicipio IS NULL OR m.codigoMunicipio = :codigoMunicipio)
             AND (:codigoUf IS NULL OR m.uf.codigoUf = :codigoUf)
             AND (:nome IS NULL OR LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
             AND (:status IS NULL OR m.status = :status)
           """)
        List<Municipio> findByFilters(
                @Param("codigoMunicipio") Long codigoMunicipio,
                @Param("codigoUf") Long codigoUf,
                @Param("nome") String nome,
                @Param("status") Integer status
        );



}
