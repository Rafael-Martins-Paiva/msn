package dev.oldschool.msn.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import dev.oldschool.msn.model.Mensagem;
import dev.oldschool.msn.service.MensagemService;

@RestController
@RequestMapping("/api/mensagens")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") 
public class MensagemController {

    private final MensagemService service;
    
    @GetMapping
    public List<Mensagem> listar() {
      return service.listar();
    }
    
    @PostMapping
    public Mensagem criar(@RequestBody Mensagem msg) {
      return service.salvar(msg);
    }
}