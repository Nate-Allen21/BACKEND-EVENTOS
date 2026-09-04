package com.senac.eventos.config;

import com.senac.eventos.model.Evento;
import com.senac.eventos.model.Usuario;
import com.senac.eventos.repository.EventoRepository;
import com.senac.eventos.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public DataLoader(EventoRepository eventoRepository, UsuarioRepository usuarioRepository) {
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        if (eventoRepository.count() == 0) {
            Evento e1 = new Evento();
            e1.setNome("Semana de Tecnologia SENAC");
            e1.setDescricao("Palestras e oficinas de TI");
            e1.setDataEvento(LocalDateTime.now().plusDays(15));
            e1.setLocal("Auditorio Principal");
            e1.setVagasTotais(50);
            e1.setVagasOcupadas(0);
            eventoRepository.save(e1);

            Evento e2 = new Evento();
            e2.setNome("Workshop de React");
            e2.setDescricao("Introducao pratica ao React");
            e2.setDataEvento(LocalDateTime.now().plusDays(5));
            e2.setLocal("Laboratorio 3");
            e2.setVagasTotais(20);
            e2.setVagasOcupadas(18);
            eventoRepository.save(e2);

            Evento e3 = new Evento();
            e3.setNome("Feira de Empreendedorismo");
            e3.setDescricao("Evento ja realizado no passado");
            e3.setDataEvento(LocalDateTime.now().minusDays(10));
            e3.setLocal("Patio Central");
            e3.setVagasTotais(100);
            e3.setVagasOcupadas(95);
            eventoRepository.save(e3);
        }

        if (usuarioRepository.findByEmailIgnoreCase("admin@eventos.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@eventos.com");
            admin.setSenha(new BCryptPasswordEncoder().encode("admin123"));
            admin.setPerfil("ADMIN");
            usuarioRepository.save(admin);
        }
    }
}
