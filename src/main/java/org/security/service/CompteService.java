package org.security.service;

import org.security.entities.Role;
import org.security.entities.Utilisateur;
import org.security.repository.RoleRepository;
import org.security.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompteService implements ICompteService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public CompteService(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        String passe = utilisateur.getModePasse();
        utilisateur.setModePasse(passwordEncoder.encode(passe));
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Role ajouterRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void ajouterRoleToUtilisateur(String pseudonyme, String nomRole) {
        Optional<Utilisateur> utilisateur = loadUtilisateurByPseudonyme(pseudonyme);
        Role role = roleRepository.findByNom(nomRole);
        utilisateur.ifPresent(p -> p.setRole(role));
    }

    @Override
    public Optional<Utilisateur> loadUtilisateurByPseudonyme(String pseudonyme) {
        return Optional.ofNullable(utilisateurRepository.findByPseudonyme(pseudonyme));
    }

    @Override
    public List<Utilisateur> listUtilisateur() {
        return utilisateurRepository.findAll();
    }
}
