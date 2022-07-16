package org.security.entities.config;

import org.security.entities.Utilisateur;
import org.security.entities.filters.JwtAuthenticationFiltre;
import org.security.service.CompteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // normalement cette class herite de la class WebSecurityConfigureAdapter mais cette dernier est deprecacted
    @Autowired
    private CompteService compteService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // désactiver la sécurité par défaut de spring
        http.csrf().disable();
        // utiliser l'authentification stateless on utilison jwt
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // uniquement dans mon cas j'utilisa h2 db je dois désactiver la sécurité des frames html
        http.headers().frameOptions().disable();
        // ce n'est pas tous les requetes qui necessite une authorization
        http.authorizeRequests().anyRequest().authenticated();
        // activier un fromulaire par defaut d'authentification de sring
        http.formLogin();
      //
          http.authorizeRequests().antMatchers("/h2console/**").permitAll();
        // ajouter filtre a la config
        http.addFilter(new JwtAuthenticationFiltre(authenticationManager));
        return http.build();
    }

    @Bean
    public UserDetailsService users(DataSource dataSource) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String pseudonyme) throws UsernameNotFoundException {
                Utilisateur utilisateur = compteService.loadUtilisateurByPseudonyme(pseudonyme);
                return new User(utilisateur.getPseudonyme(), utilisateur.getModePasse(), Collections.singleton(new SimpleGrantedAuthority(utilisateur.getRole().getNom())));
            }
        };
    }

}
