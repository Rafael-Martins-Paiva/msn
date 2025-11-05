package dev.oldschool.msn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.oldschool.msn.model.Mensagem;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    List<Mensagem> findByRoomId(Long roomId);
}