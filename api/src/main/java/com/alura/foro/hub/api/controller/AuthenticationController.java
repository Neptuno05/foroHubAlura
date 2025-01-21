package com.alura.foro.hub.api.controller;

import com.alura.foro.hub.api.domain.user.AutenticationUser;
import com.alura.foro.hub.api.domain.user.User;
import com.alura.foro.hub.api.infra.security.JWTtokenData;
import com.alura.foro.hub.api.infra.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación de usuarios y la obtención de tokens de acceso.")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Operation(
            summary = "Autenticar usuario",
            description = "Autentica al usuario con las credenciales proporcionadas y genera un token JWT para el acceso.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales del usuario para la autenticación",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AutenticationUser.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de autenticación de usuario",
                                    value = "{ \"email\": \"usuario@example.com\", \"clave\": \"password123\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticación exitosa y token generado", content = @Content(schema = @Schema(implementation = JWTtokenData.class))),
                    @ApiResponse(responseCode = "400", description = "Credenciales inválidas"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    public ResponseEntity<JWTtokenData> autenticarUsuario(@RequestBody @Valid AutenticationUser autenticatioUser){
        Authentication authToken = new UsernamePasswordAuthenticationToken(autenticatioUser.email(),
                autenticatioUser.clave());
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        var JWTtoken = tokenService.generarToken((User) usuarioAutenticado.getPrincipal());

        return ResponseEntity.ok(new JWTtokenData(JWTtoken));
    }

}
