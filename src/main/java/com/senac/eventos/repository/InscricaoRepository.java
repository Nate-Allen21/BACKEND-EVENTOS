package com.senac.eventos.repository;

import com.senac.eventos.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    List<Inscricao> findByEventoId(Long eventoId);
}
