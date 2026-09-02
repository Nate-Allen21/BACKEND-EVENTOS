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
        Evento evento = eventoRepository.findById(inscricao.getEventoId()).orElseThrow();

        // BUG: nao verifica se vagasOcupadas < vagasTotais antes de inscrever.
        // O sistema aceita inscricoes infinitas, mesmo com o evento lotado.
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setStatus(StatusInscricao.CONFIRMADA);
        Inscricao salva = inscricaoRepository.save(inscricao);

        evento.setVagasOcupadas(evento.getVagasOcupadas() + 1);
        eventoRepository.save(evento);

        return salva;
    }

    public void cancelar(Long id) {
        // BUG (design): a inscricao tem um enum de status (CONFIRMADA/CANCELADA)
        // mas o cancelamento aqui DELETA o registro do banco em vez de apenas
        // mudar o status para CANCELADA. Isso apaga o historico de inscricoes
        // canceladas, e alem disso...
        Inscricao inscricao = inscricaoRepository.findById(id).orElseThrow();
        inscricaoRepository.deleteById(id);

        // BUG: ...esquece de decrementar vagasOcupadas do evento ao cancelar,
        // entao a vaga liberada nunca volta a ficar disponivel para outra pessoa.
    }
}
