package com.senac.eventos.repository;

import com.senac.eventos.model.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    Optional<Participante> findByEmailIgnoreCase(String email);
}
