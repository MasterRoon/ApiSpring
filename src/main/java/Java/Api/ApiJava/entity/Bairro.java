package Java.Api.ApiJava.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "TB_BAIRRO")
public class Bairro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "sequence_Bairro_generator")
    @SequenceGenerator(name = "sequence_Bairro_generator", sequenceName = "Sequence_Bairro", allocationSize = 1)
    @Column(name = "CODIGO_BAIRRO")
    private Long codigoBairro;

    @Column(name = "NOME", length = 256)
    private String nome;

    @Column(name = "STATUS")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "CODIGO_MUNICIPIO")
    private Municipio municipio;

    @OneToMany(mappedBy = "bairro")
    private Set<Endereco> enderecos;

    @CreationTimestamp
    private Instant creationTimestamp;
    @UpdateTimestamp
    private Instant updateTimestamp;

    public Bairro() {
    }

    public Bairro(Long codigoBairro, String nome, Integer status, Municipio municipio, Set<Endereco> enderecos, Instant creationTimestamp, Instant updateTimestamp) {
        this.codigoBairro = codigoBairro;
        this.nome = nome;
        this.status = status;
        this.municipio = municipio;
        this.enderecos = enderecos;
        this.creationTimestamp = creationTimestamp;
        this.updateTimestamp = updateTimestamp;
    }

    public Long getCodigoBairro() {
        return codigoBairro;
    }

    public void setCodigoBairro(Long codigoBairro) {
        this.codigoBairro = codigoBairro;
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

    public Set<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
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
