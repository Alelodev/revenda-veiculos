package br.com.revenda.repository;

import br.com.revenda.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    List<Veiculo> findByUsuarioIdUsuario(Long idUsuario);
    boolean existsByPlaca(String placa);
}
