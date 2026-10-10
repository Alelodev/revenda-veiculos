package br.com.revenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento", nullable = false)
    private Long idDocumento;

    @ManyToOne
    @JoinColumn(name = "id_veiculo")
    @NotNull(message = "Veículo é obrigatório")
    private Veiculo veiculo;

    @Column(name = "nome", nullable = false)
    @NotBlank(message = "Nome do documento é obrigatório")
    private String nome;

    @Column(name = "tipo_documento", nullable = false)
    @NotBlank(message = "Tipo do documento é obrigatório")
    private String tipoDocumento;

    @Column(name = "caminho_arquivo", nullable = false)
    @NotBlank(message = "Caminho do arquivo é obrigatório")
    private String caminhoArquivo;

    @Column(name = "data_upload", nullable = false, insertable = false, updatable = false)
    private LocalDateTime dataUpload;

    public Long getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Long idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }
}
