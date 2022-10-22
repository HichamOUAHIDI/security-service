package org.security.repository;

import org.security.entities.Compte;
import org.security.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<Compte,Long> {
    Compte findByPseudonyme (String pseudonyme);
}
