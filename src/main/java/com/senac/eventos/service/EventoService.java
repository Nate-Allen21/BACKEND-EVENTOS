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
        return eventoRepository.findById(id).orElseThrow();
    }

    public Evento salvar(Evento evento) {
        if (evento.getVagasOcupadas() == null) {
            evento.setVagasOcupadas(0);
        }
        return eventoRepository.save(evento);
    }

    public void excluir(Long id) {
        eventoRepository.deleteById(id);
    }

    public boolean jaEncerrado(Evento evento) {
        // BUG: usa equals() para comparar datas/horarios exatos, o que na pratica
        // nunca é verdadeiro (a hora atual nunca bate exatamente com a data do
        // evento). Deveria usar isBefore(). Resultado: eventos passados nunca
        // sao marcados como encerrados.
        return evento.getDataEvento().equals(LocalDateTime.now());
    }

    public int vagasDisponiveis(Evento evento) {
        // BUG: subtrai 1 a mais do que deveria (off-by-one), entao o sistema
        // sempre mostra uma vaga a menos do que realmente existe.
        return evento.getVagasTotais() - evento.getVagasOcupadas() - 1;
    }
}
