package dev.oldschool.msn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.oldschool.msn.model.Mensagem;

public interface MensagemRepository extends JpaRepository<Mensagem Long> {
}