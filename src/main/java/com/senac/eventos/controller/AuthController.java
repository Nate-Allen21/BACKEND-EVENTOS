package com.senac.eventos.controller;

import com.senac.eventos.model.Usuario;
import com.senac.eventos.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrar(@RequestBody Usuario usuario) {
        Usuario salvo = usuarioService.registrar(usuario);
        return ResponseEntity.ok(Map.of(
            "id", salvo.getId(),
            "nome", salvo.getNome(),
            "email", salvo.getEmail(),
            "perfil", salvo.getPerfil()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String senha = payload.get("senha");

        Usuario usuario = usuarioService.login(email, senha);
        return ResponseEntity.ok(Map.of(
            "id", usuario.getId(),
            "nome", usuario.getNome(),
            "email", usuario.getEmail(),
            "perfil", usuario.getPerfil()
        ));
    }
}
