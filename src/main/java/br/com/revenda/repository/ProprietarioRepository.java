package br.com.revenda.repository;

import br.com.revenda.model.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {

}
