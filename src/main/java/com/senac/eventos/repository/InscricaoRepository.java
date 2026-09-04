package com.senac.eventos.repository;

import com.senac.eventos.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    List<Inscricao> findByEventoId(Long eventoId);
    Optional<Inscricao> findByEventoIdAndParticipanteId(Long eventoId, Long participanteId);
}
