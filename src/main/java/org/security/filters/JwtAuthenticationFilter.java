package org.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String PSEUDONYME ="pseudonyme";
    private static final String MODEPASSE ="modePasse";
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        var username = request.getParameter(PSEUDONYME);
        var passowrd = request.getParameter(MODEPASSE);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,passowrd);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("my-secret-token-bricol");
        String jwt_AccessToken= JWT.create()
                .withSubject(user.getUsername())
                //timeout courte
                .withExpiresAt(new Date(System.currentTimeMillis()+15*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(String::toString)
                        .findFirst()
                        .orElse(null))
                .sign(algorithm);

        // pour le problème de revocation du token on crée le refreshToken
        String jwt_RefreshToken= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+15*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        Map<String,String> idToken = new HashMap<>();
        idToken.put("access-token", jwt_AccessToken);
        idToken.put("refresh-token", jwt_RefreshToken);
        new ObjectMapper().writeValue(response.getOutputStream(),idToken);
        response.setContentType("application/json");
    }
}
