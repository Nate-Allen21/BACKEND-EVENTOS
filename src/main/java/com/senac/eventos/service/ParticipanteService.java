package com.senac.eventos.service;

import com.senac.eventos.model.Participante;
import com.senac.eventos.repository.ParticipanteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipanteService {

    private final ParticipanteRepository participanteRepository;

    public ParticipanteService(ParticipanteRepository participanteRepository) {
        this.participanteRepository = participanteRepository;
    }

    public List<Participante> listarTodos() {
        return participanteRepository.findAll();
    }

    public Participante salvar(Participante participante) {
        if (participante == null) {
            throw new IllegalArgumentException("Participante obrigatório");
        }
        if (participante.getEmail() == null || participante.getEmail().isBlank()) {
            throw new IllegalArgumentException("E-mail obrigatório");
        }
        if (participante.getCpf() == null || participante.getCpf().isBlank()) {
            throw new IllegalArgumentException("CPF obrigatório");
        }

        participanteRepository.findByEmailIgnoreCase(participante.getEmail())
            .ifPresent(existing -> {
                throw new IllegalStateException("Já existe um participante com este e-mail");
            });

        return participanteRepository.save(participante);
    }
}
