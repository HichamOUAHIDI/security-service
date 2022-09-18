package org.security.service;

import org.security.entities.Utilisateur;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.String.format;

@Service
public class UserDetailService implements UserDetailsService {

    private final ICompteService ICompteService;

    public UserDetailService(ICompteService ICompteService) {
        this.ICompteService = ICompteService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = ICompteService.loadUtilisateurByPseudonyme(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        format("User: %s, not found", username)
                ));
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(utilisateur.getRole().getNom()));
        return new User(utilisateur.getPseudonyme(), utilisateur.getModePasse(), authorities);
    }
}
