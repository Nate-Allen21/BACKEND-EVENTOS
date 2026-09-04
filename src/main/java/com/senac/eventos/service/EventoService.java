package com.senac.eventos.service;

import com.senac.eventos.model.Evento;
import com.senac.eventos.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Evento não encontrado: " + id));
    }

    public Evento salvar(Evento evento) {
        if (evento.getVagasOcupadas() == null) {
            evento.setVagasOcupadas(0);
        }
        if (evento.getVagasTotais() == null || evento.getVagasTotais() < 0) {
            throw new IllegalArgumentException("Número de vagas inválido");
        }
        return eventoRepository.save(evento);
    }

    public void excluir(Long id) {
        eventoRepository.deleteById(id);
    }

    public boolean jaEncerrado(Evento evento) {
        return evento.getDataEvento() != null && evento.getDataEvento().isBefore(LocalDateTime.now());
    }

    public int vagasDisponiveis(Evento evento) {
        Integer vagasTotais = evento.getVagasTotais();
        Integer vagasOcupadas = evento.getVagasOcupadas();

        if (vagasTotais == null) {
            return 0;
        }
        if (vagasOcupadas == null) {
            vagasOcupadas = 0;
        }

        return Math.max(vagasTotais - vagasOcupadas, 0);
    }
}
