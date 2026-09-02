package com.senac.eventos.repository;

import com.senac.eventos.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
}
