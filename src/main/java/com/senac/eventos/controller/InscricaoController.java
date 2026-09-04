package com.senac.eventos.controller;

import com.senac.eventos.model.Inscricao;
import com.senac.eventos.service.InscricaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscricoes")
@CrossOrigin(origins = "*")
public class InscricaoController {

    private final InscricaoService inscricaoService;

    public InscricaoController(InscricaoService inscricaoService) {
        this.inscricaoService = inscricaoService;
    }

    @GetMapping
    public List<Inscricao> listarPorEvento(@RequestParam Long eventoId) {
        return inscricaoService.listarPorEvento(eventoId);
    }

    @PostMapping
    public Inscricao inscrever(@RequestBody Inscricao inscricao) {
        return inscricaoService.inscrever(inscricao);
    }

    @DeleteMapping("/{id}")
    public void cancelar(@PathVariable Long id) {
        inscricaoService.cancelar(id);
    }
}
