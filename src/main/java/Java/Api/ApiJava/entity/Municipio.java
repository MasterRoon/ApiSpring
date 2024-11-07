package Java.Api.ApiJava.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "TB_MUNICIPIO")
public class Municipio {

    @Id
    @Column(name = "CODIGO_MUNICIPIO")
    private Long codigoMunicipio;

    @Column(name = "NOME", length = 256)
    private String nome;

    @Column(name = "STATUS")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "CODIGO_UF")
    private Uf uf;

    @OneToMany(mappedBy = "municipio")
    private Set<Bairro> bairros;

    @CreationTimestamp
    private Instant creationTimestamp;
    @UpdateTimestamp
    private Instant updateTimestamp;

    public Municipio() {
    }

    public Municipio(Long codigoMunicipio, String nome, Integer status, Uf uf, Set<Bairro> bairros, Instant creationTimestamp, Instant updateTimestamp) {
        this.codigoMunicipio = codigoMunicipio;
        this.nome = nome;
        this.status = status;
        this.uf = uf;
        this.bairros = bairros;
        this.creationTimestamp = creationTimestamp;
        this.updateTimestamp = updateTimestamp;
    }

    public Long getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public void setCodigoMunicipio(Long codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
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

    public Set<Bairro> getBairros() {
        return bairros;
    }

    public void setBairros(Set<Bairro> bairros) {
        this.bairros = bairros;
    }

    public Uf getUf() {
        return uf;
    }

    public void setUf(Uf uf) {
        this.uf = uf;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

