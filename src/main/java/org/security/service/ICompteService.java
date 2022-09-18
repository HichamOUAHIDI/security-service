package org.security.service;

import org.security.entities.Role;
import org.security.entities.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface ICompteService {
    Utilisateur ajouterUtilisateur (Utilisateur utilisateur);
    Role ajouterRole (Role role);
    void ajouterRoleToUtilisateur(String pseudonyme, String nomRole);
    Optional<Utilisateur> loadUtilisateurByPseudonyme(String pseudonyme);
    List<Utilisateur> listUtilisateur();
}
