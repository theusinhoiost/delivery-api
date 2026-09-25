package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.login.LoginRequestDTO;
import com.deliverytech.delivery_api.dto.login.LoginResponseDTO;
import com.deliverytech.delivery_api.dto.restaurante.RegisterRequestDTO;
import com.deliverytech.delivery_api.dto.user.UserResponseDTO;
import com.deliverytech.delivery_api.entity.Usuario;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.security.JwtUtil;
import com.deliverytech.delivery_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints de cadastro e login de usuários com emissão de token JWT")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cadastra um novo usuário no sistema (ADMIN, CLIENTE, RESTAURANTE, ENTREGADOR)")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        if (authService.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + request.getEmail());
        }
        Usuario usuario = authService.criarUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(usuario));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Realiza login com email e senha e retorna o token JWT para autorização")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtUtil.generateToken(usuario);

        LoginResponseDTO response = new LoginResponseDTO(
                token,
                jwtUtil.getExpiration(),
                new UserResponseDTO(usuario));

        return ResponseEntity.ok(response);
    }
}
