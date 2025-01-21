package com.alura.foro.hub.api.infra.service;

import com.alura.foro.hub.api.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class TokenService {
    private final String issuerTitle = "Foro Hub Alura";
    @Value("${api.security.token.secret}")
    private String apiSecret;

    public String generarToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer(issuerTitle)
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId())
                    .withExpiresAt(generarFechaVencimiento())
                    .sign(algorithm);
        }catch (JWTCreationException e){
            throw new RuntimeException("Failed to generate token");
        }

    }

    public String getSubject(String token){
        if (token == null){
            throw new RuntimeException("Token is null");
        }

        DecodedJWT verifier = null;

        //validar firma
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            verifier = JWT.require(algorithm)
                    .withIssuer(issuerTitle)
                    .build()
                    .verify(token);
            verifier.getSubject();

        }catch (JWTVerificationException e){
            System.out.println(e.toString());
        }
        if (verifier.getSubject() == null){
            throw new RuntimeException("Invalid verifier");
        }
        return verifier.getSubject();
    }

    private Instant generarFechaVencimiento() {
        ZoneId zonaHorariaPredeterminada = ZoneId.systemDefault();
        return ZonedDateTime.now(zonaHorariaPredeterminada).plusHours(2).toInstant();
    }
}
