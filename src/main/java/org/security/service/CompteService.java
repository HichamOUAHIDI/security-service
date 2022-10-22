package org.security.service;

import org.security.entities.Compte;
import org.security.repository.CompteRepository;
import org.security.service.dto.CompteDto;
import org.security.service.dto.Roles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompteService implements ICompteService {

    private final CompteRepository compteRepository;

    private final PasswordEncoder passwordEncoder;

    public CompteService(CompteRepository compteRepository,  PasswordEncoder passwordEncoder) {
        this.compteRepository = compteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Compte ajouterCompteUtilisateur(Compte compte) {
        compte.setModePasse(passwordEncoder.encode(compte.getModePasse()));
        ajouterRoleToCompte(compte, Roles.USER.value());
        return compteRepository.save(compte);
    }


    @Override
    public void ajouterRoleToCompte(Compte compte, String nomRole) {
        compte.getUtilisateur().setRole(nomRole);
    }

    @Override
    public Optional<Compte> loadCompteByPseudonyme(String pseudonyme) {
        return Optional.ofNullable(compteRepository.findByPseudonyme(pseudonyme));
    }

    @Override
    public List<CompteDto> listCompte() {
        var comptes= compteRepository.findAll();
        List<CompteDto> compteDtos = new ArrayList<>();
        comptes.forEach(compte -> {
            CompteDto compteDto = new CompteDto();
            compteDto.setNom(compte.getUtilisateur().getNom());
            compteDto.setPrenom(compte.getUtilisateur().getPrenom());
            compteDtos.add(compteDto);
        });
        return compteDtos;
    }
}
