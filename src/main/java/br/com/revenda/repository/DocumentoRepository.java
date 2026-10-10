package br.com.revenda.repository;

import br.com.revenda.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByVeiculoIdVeiculo(Long idVeiculo);
}
