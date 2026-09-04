package com.senac.eventos.service;

import com.senac.eventos.model.Evento;
import com.senac.eventos.model.Inscricao;
import com.senac.eventos.model.StatusInscricao;
import com.senac.eventos.repository.EventoRepository;
import com.senac.eventos.repository.InscricaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InscricaoService {

    private final InscricaoRepository inscricaoRepository;
    private final EventoRepository eventoRepository;

    public InscricaoService(InscricaoRepository inscricaoRepository, EventoRepository eventoRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.eventoRepository = eventoRepository;
    }

    public List<Inscricao> listarPorEvento(Long eventoId) {
        return inscricaoRepository.findByEventoId(eventoId);
    }

    public Inscricao inscrever(Inscricao inscricao) {
        if (inscricao == null) {
            throw new IllegalArgumentException("Inscrição obrigatória");
        }

        Evento evento = eventoRepository.findById(inscricao.getEventoId()).orElseThrow(
            () -> new IllegalArgumentException("Evento não encontrado: " + inscricao.getEventoId())
        );

        if (evento.getVagasOcupadas() == null) {
            evento.setVagasOcupadas(0);
        }

        if (evento.getVagasTotais() != null && evento.getVagasOcupadas() >= evento.getVagasTotais()) {
            throw new IllegalStateException("Evento lotado");
        }

        inscricaoRepository.findByEventoIdAndParticipanteId(inscricao.getEventoId(), inscricao.getParticipanteId())
            .ifPresent(existing -> {
                throw new IllegalStateException("Participante já inscrito neste evento");
            });

        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setStatus(StatusInscricao.CONFIRMADA);
        Inscricao salva = inscricaoRepository.save(inscricao);

        evento.setVagasOcupadas(evento.getVagasOcupadas() + 1);
        eventoRepository.save(evento);

        return salva;
    }

    public void cancelar(Long id) {
        Inscricao inscricao = inscricaoRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Inscrição não encontrada: " + id)
        );

        if (inscricao.getStatus() == StatusInscricao.CANCELADA) {
            return;
        }

        inscricao.setStatus(StatusInscricao.CANCELADA);
        inscricaoRepository.save(inscricao);

        Evento evento = eventoRepository.findById(inscricao.getEventoId()).orElse(null);
        if (evento != null && evento.getVagasOcupadas() != null && evento.getVagasOcupadas() > 0) {
            evento.setVagasOcupadas(evento.getVagasOcupadas() - 1);
            eventoRepository.save(evento);
        }
    }
}
