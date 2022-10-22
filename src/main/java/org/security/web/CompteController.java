package org.security.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.security.entities.Compte;
import org.security.service.ICompteService;
import org.security.service.dto.CompteDto;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.lang.String.format;

@RestController
public class CompteController {
    private final ICompteService ICompteService;

    public CompteController(ICompteService ICompteService) {
        this.ICompteService = ICompteService;
    }

    @GetMapping(path = "/comptes")
    @PostAuthorize("hasAuthority('USER')")
    public List<CompteDto>  listCompte() {
        return ICompteService.listCompte();
    }

    @PostMapping(path = "/createComptes")
    public Compte save(@RequestBody Compte compte) {
        return ICompteService.ajouterCompteUtilisateur(compte);
    }

    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationToken = request.getHeader("Authorization");
        if (Objects.nonNull(authorizationToken) && authorizationToken.startsWith("Bearer ")) {
            try {
                String jwt = authorizationToken.substring(7);
                Algorithm algorithm = Algorithm.HMAC256("my-secret-token-bricol");
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String pseudonyme = decodedJWT.getSubject();
                Compte compte = ICompteService.loadCompteByPseudonyme(pseudonyme).orElseThrow(
                        () -> new UsernameNotFoundException(
                                format("User: %s, not found", pseudonyme)
                        ));

                String jwt_AccessToken = JWT.create()
                        .withSubject(compte.getPseudonyme())
                        //timeout courte
                        .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", compte.getUtilisateur().getRole())
                        .sign(algorithm);
                Map<String, String> idToken = new HashMap<>();
                idToken.put("access-token", jwt_AccessToken);
                idToken.put("refresh-token", jwt);
                new ObjectMapper().writeValue(response.getOutputStream(), idToken);
                response.setContentType("application/json");
            } catch (Exception e) {
                response.setHeader("error-message", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            throw new RuntimeException("Refresh Token Required !! ");
        }
    }
}
