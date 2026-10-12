package br.com.revenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "login", nullable = false, unique = true)
    @NotBlank(message = "Login é obrigatorio!")
    private String login;

    @Column(name = "senha", nullable = false)
    @NotBlank(message = "Senha é obrigatoria!")
    private String senha;

    @Column(name = "perfil", nullable = false, length = 20)
    @NotBlank(message = "Perfil é obrigatório")
    private String perfil;

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
