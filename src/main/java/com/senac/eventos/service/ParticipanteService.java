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
        // BUG: nao verifica se ja existe um participante com o mesmo e-mail
        // antes de salvar, entao a mesma pessoa pode ser cadastrada varias
        // vezes com o mesmo e-mail (ou ate com cpf vazio, pois o campo cpf
        // nao tem nenhuma validacao de formato/obrigatoriedade).
        return participanteRepository.save(participante);
    }
}
