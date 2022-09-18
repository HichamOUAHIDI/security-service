package org.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    // intercepteur springSecurty pour chaque requet passe par ce filtre avan le dispatcherServelet
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/refreshToken") || request.getServletPath().equals("/login")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationToken = request.getHeader("Authorization");
            if (Objects.nonNull(authorizationToken) && authorizationToken.startsWith("Bearer ")) {
                try {
                    // supprimer la chain bearer pour vérifier que le token
                    String jwt = authorizationToken.substring(7);
                    // vérifier la signature avec le meme secert le principe de l'algo HMAC sinon pour RSA il faut une autre clé de vérification
                    Algorithm algorithm = Algorithm.HMAC256("my-secret-token-bricol");
                    // j'utilise la methode verify de jwt pour vérifier l'algorithem
                    JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                    // si pas de problème je prépare l'authentication
                    String username = decodedJWT.getSubject();
                    String role = decodedJWT.getClaim("roles").asString();
                    Collection<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    // je demande a spring d'authoriser ce utilisateur
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    // je demande a spring de passer au filtre suivant
                    // derrière spring passe le traitement soit a d'autre filtre ou ver le dispatcherServelet
                    filterChain.doFilter(request, response);
                } catch (RuntimeException e) {
                    throw  new RuntimeException("Error authentication");
                }
            } else {
                // dans ce cas je passe la requet, c'est spring sécurity qui vas vérifié si l'acees a cette resource necessite une authentification ou non
                filterChain.doFilter(request, response);
            }
        }
    }
}
