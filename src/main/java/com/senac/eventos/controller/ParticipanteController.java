package com.senac.eventos.controller;

import com.senac.eventos.model.Participante;
import com.senac.eventos.service.ParticipanteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participantes")
@CrossOrigin(origins = "*")
public class ParticipanteController {

    private final ParticipanteService participanteService;

    public ParticipanteController(ParticipanteService participanteService) {
        this.participanteService = participanteService;
    }

    @GetMapping
    public List<Participante> listar() {
        return participanteService.listarTodos();
    }

    @PostMapping
    public Participante criar(@RequestBody Participante participante) {
        return participanteService.salvar(participante);
    }
}
