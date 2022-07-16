package org.security.service;

import org.security.entities.Role;
import org.security.entities.Utilisateur;

import java.util.List;

public interface CompteService {
    Utilisateur ajouterUtilisateur (Utilisateur utilisateur);
    Role ajouterRole (Role role);
    void ajouterRoleToUtilisateur(String pseudonyme, String nomRole);
    Utilisateur loadUtilisateurByPseudonyme(String pseudonyme);
    List<Utilisateur> listUtilisateur();
}
