package Java.Api.ApiJava.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "TB_ENDERECO")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "sequence_Endereco_generator")
    @SequenceGenerator(name = "sequence_Endereco_generator", sequenceName = "Sequence_Endereco", allocationSize = 1)
    @Column(name = "CODIGO_ENDERECO")
    private Long codigoEndereco;

    @ManyToOne
    @JoinColumn(name = "CODIGO_PESSOA", nullable = false)
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "CODIGO_BAIRRO", nullable = false)
    private Bairro bairro;

    @Column(name = "NOME_RUA", length = 210)
    private String nomeRua;

    @Column(name = "NUMERO")
    private Integer numero;

    @Column(name = "COMPLEMENTO", length = 210)
    private String complemento;

    @Column(name = "CEP", length = 10)
    private String cep;

    @CreationTimestamp
    private Instant creationTimestamp;
    @UpdateTimestamp
    private Instant updateTimestamp;

    public Endereco() {
    }

    public Endereco(Long codigoEndereco, Instant updateTimestamp, Instant creationTimestamp, String cep, String complemento, String nomeRua, Integer numero, Bairro bairro, Pessoa pessoa) {
        this.codigoEndereco = codigoEndereco;
        this.updateTimestamp = updateTimestamp;
        this.creationTimestamp = creationTimestamp;
        this.cep = cep;
        this.complemento = complemento;
        this.nomeRua = nomeRua;
        this.numero = numero;
        this.bairro = bairro;
        this.pessoa = pessoa;
    }

    public Long getCodigoEndereco() {
        return codigoEndereco;
    }

    public void setCodigoEndereco(Long codigoEndereco) {
        this.codigoEndereco = codigoEndereco;
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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNomeRua() {
        return nomeRua;
    }

    public void setNomeRua(String nomeRua) {
        this.nomeRua = nomeRua;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }


}
