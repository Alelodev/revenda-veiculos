package br.com.revenda.model;

import jakarta.persistence.*;

@Entity
public class Proprietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proprietario", nullable = false)
    private Long idProprietario;


    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto;
    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "telefone", length = 20)
    private String telefone;
    @Column(name = "endereco", length = 255)
    private String endereco;
    @Column(name = "email", length = 150)
    private String email;
    @Column(name = "cnh", length = 20)
    private String cnh;


    public Long getIdProprietario() {
        return idProprietario;
    }

    public void setIdProprietario(Long idProprietario) {
        this.idProprietario = idProprietario;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }
}
