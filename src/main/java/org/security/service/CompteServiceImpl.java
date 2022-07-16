package org.security.service;

import org.security.entities.Role;
import org.security.entities.Utilisateur;
import org.security.repository.RoleRepository;
import org.security.repository.UtilisateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CompteServiceImpl implements CompteService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public CompteServiceImpl(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
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
        Utilisateur utilisateur = loadUtilisateurByPseudonyme(pseudonyme);
        Role role = roleRepository.findByNom(nomRole);
        utilisateur.setRole(role);
    }

    @Override
    public Utilisateur loadUtilisateurByPseudonyme(String pseudonyme) {
        return utilisateurRepository.findByPseudonyme(pseudonyme);
    }

    @Override
    public List<Utilisateur> listUtilisateur() {
        return utilisateurRepository.findAll();
    }
}
