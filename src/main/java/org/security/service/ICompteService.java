package org.security.service;

import org.security.entities.Compte;
import org.security.service.dto.CompteDto;

import java.util.List;
import java.util.Optional;

public interface ICompteService {
    Compte ajouterCompteUtilisateur(Compte compte);

    void ajouterRoleToCompte(Compte compte, String nomRole);

    Optional<Compte> loadCompteByPseudonyme(String pseudonyme);

    List<CompteDto> listCompte();
}
