package com.senac.eventos.service;

import com.senac.eventos.model.Evento;
import com.senac.eventos.model.Inscricao;
import com.senac.eventos.model.Participante;
import com.senac.eventos.repository.EventoRepository;
import com.senac.eventos.repository.InscricaoRepository;
import com.senac.eventos.repository.ParticipanteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceAuditTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private InscricaoRepository inscricaoRepository;

    @Mock
    private ParticipanteRepository participanteRepository;

    @Test
    void deveMarcarEventoComoEncerradoQuandoADataJaPassou() {
        EventoService service = new EventoService(eventoRepository);
        Evento evento = new Evento();
        evento.setDataEvento(LocalDateTime.now().minusMinutes(5));

        assertTrue(service.jaEncerrado(evento));
    }

    @Test
    void deveCalcularVagasDisponiveisSemOffByOne() {
        EventoService service = new EventoService(eventoRepository);
        Evento evento = new Evento();
        evento.setVagasTotais(10);
        evento.setVagasOcupadas(3);

        assertEquals(7, service.vagasDisponiveis(evento));
    }

    @Test
    void deveRecusarInscricaoQuandoEventoEstaLotado() {
        InscricaoService service = new InscricaoService(inscricaoRepository, eventoRepository);
        Evento evento = new Evento();
        evento.setVagasTotais(2);
        evento.setVagasOcupadas(2);

        Inscricao inscricao = new Inscricao();
        inscricao.setEventoId(1L);
        inscricao.setParticipanteId(99L);

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        assertThrows(IllegalStateException.class, () -> service.inscrever(inscricao));
    }

    @Test
    void deveRejeitarParticipanteComEmailDuplicado() {
        ParticipanteService service = new ParticipanteService(participanteRepository);
        Participante participante = new Participante();
        participante.setNome("Ana");
        participante.setEmail("ana@email.com");
        participante.setCpf("12345678909");

        when(participanteRepository.findByEmailIgnoreCase("ana@email.com")).thenReturn(Optional.of(new Participante()));

        assertThrows(IllegalStateException.class, () -> service.salvar(participante));
    }
}
