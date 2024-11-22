package Java.Api.ApiJava.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "TB_UF")
public class Uf {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_Uf_generator")
    @SequenceGenerator(name = "sequence_Uf_generator", sequenceName = "Sequence_Uf", allocationSize = 1)
    @Column(name = "CODIGO_UF")
    private Long codigoUf;

    @Column(name = "SIGLA", length = 2)
    private String sigla;

    @Column(name = "NOME", length = 60)
    private String nome;

    @Column(name = "STATUS")
    private Integer status;

    @OneToMany(mappedBy = "uf")
    private Set<Municipio> municipios;

    @CreationTimestamp
    private Instant creationTimestamp;
    @UpdateTimestamp
    private Instant updateTimestamp;

    public Uf() {
    }

    public Uf(Long codigoUf, Instant updateTimestamp, Integer status, String sigla, String nome, Set<Municipio> municipios, Instant creationTimestamp) {
        this.codigoUf = codigoUf;
        this.updateTimestamp = updateTimestamp;
        this.status = status;
        this.sigla = sigla;
        this.nome = nome;
        this.municipios = municipios;
        this.creationTimestamp = creationTimestamp;
    }

    public Long getCodigoUf() {
        return codigoUf;
    }

    public void setCodigoUf(Long codigoUf) {
        this.codigoUf = codigoUf;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Set<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(Set<Municipio> municipios) {
        this.municipios = municipios;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
