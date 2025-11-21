package dev.oldschool.msn.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import dev.oldschool.msn.model.Mensagem;
import dev.oldschool.msn.repository.MensagemRepository;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MensagemRepository repo;
    
    public List<Mensagem> listar(){
      return repo.findAll();
    }

    public List<Mensagem> listarPorSala(Long roomId){
      return repo.findByRoomId(roomId);
    }
    
    public Mensagem salvar(Mensagem mensagem) {
      if (mensagem.getRoomId() == null) {
          throw new IllegalArgumentException("Room ID must be provided for messages.");
      }
      return repo.save(mensagem);
    }
}