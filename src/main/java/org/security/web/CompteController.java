package org.security.web;

import org.security.entities.Utilisateur;
import org.security.service.CompteService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.List;

@RestController
public class CompteController {
    private final CompteService compteService;


    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }
    @GetMapping( path = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Utilisateur> listOfUtilisateur(){
        return compteService.listUtilisateur();
    }

    @PostMapping(path = "/users")
    public Utilisateur save(@RequestBody Utilisateur utilisateur){
        return compteService.ajouterUtilisateur(utilisateur);
    }
}
