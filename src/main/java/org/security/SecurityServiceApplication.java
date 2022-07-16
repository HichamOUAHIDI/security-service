package org.security;

import org.security.entities.Role;
import org.security.entities.Utilisateur;
import org.security.service.CompteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SecurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CompteService compteService) {
        return args -> {
            compteService.ajouterRole(new Role(null, "USER"));
            compteService.ajouterRole(new Role(null, "ADMIN"));
            compteService.ajouterUtilisateur(new Utilisateur(null, "Hicham", "1234", null));
            compteService.ajouterUtilisateur(new Utilisateur(null, "Ouahidi", "4321", null));
			compteService.ajouterRoleToUtilisateur("Hicham","USER");
            compteService.ajouterRoleToUtilisateur("Ouahidi","ADMIN");
        };
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
