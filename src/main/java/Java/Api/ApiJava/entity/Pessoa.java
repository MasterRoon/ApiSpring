package Java.Api.ApiJava.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "TB_PESSOA")
public class Pessoa {

    @Id

    @Column(name = "CODIGO_PESSOA")
    private Long codigoPessoa;

    @Column(name = "NOME", length = 256)
    private String nome;

    @Column(name = "SOBRENOME", length = 256)
    private String sobrenome;

    @Column(name = "IDADE")
    private Integer idade;

    @Column(name = "LOGIN", length = 50)
    private String login;

    @Column(name = "SENHA", length = 250)
    private String senha;

    @Column(name = "STATUS")
    private Integer status;

    @OneToMany(mappedBy = "pessoa")
    private Set<Endereco> enderecos;

    @CreationTimestamp
    private Instant creationTimestamp;
    @UpdateTimestamp
    private Instant updateTimestamp;

    public Pessoa() {
    }

    public Pessoa(Long codigoPessoa, Instant updateTimestamp, Instant creationTimestamp, Set<Endereco> enderecos, Integer status, String senha, String login, Integer idade, String sobrenome, String nome) {
        this.codigoPessoa = codigoPessoa;
        this.updateTimestamp = updateTimestamp;
        this.creationTimestamp = creationTimestamp;
        this.enderecos = enderecos;
        this.status = status;
        this.senha = senha;
        this.login = login;
        this.idade = idade;
        this.sobrenome = sobrenome;
        this.nome = nome;
    }

    public Long getCodigoPessoa() {
        return codigoPessoa;
    }

    public void setCodigoPessoa(Long codigoPessoa) {
        this.codigoPessoa = codigoPessoa;
    }

    public Set<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Instant creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Instant getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Instant updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
