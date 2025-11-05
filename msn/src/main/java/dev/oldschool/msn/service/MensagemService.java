package dev.oldschool.chatapp.service;

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
    
    public Mensagem salvar(Mensagem mensagem) {
      return repo.save(mensagem);
    }
}