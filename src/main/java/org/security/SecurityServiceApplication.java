package org.security;

import org.security.entities.Compte;
import org.security.entities.Utilisateur;
import org.security.service.ICompteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SecurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(ICompteService ICompteService) {
        return args -> {
            Utilisateur utilisateur1 = new Utilisateur();
            utilisateur1.setPrenom("Hicham");
            utilisateur1.setNom("TEST");
            Utilisateur utilisateur2 = new Utilisateur();
            utilisateur2.setPrenom("OUAHIDI");
            utilisateur2.setNom("TEST2");
            Compte compte1 = new Compte();
            compte1.setPseudonyme("pseudoHO");
            compte1.setModePasse("1234");
            Compte compte2 = new Compte();
            compte2.setPseudonyme("pseudoOUA");
            compte2.setModePasse("4321");
            compte1.setUtilisateur(utilisateur1);
            compte2.setUtilisateur(utilisateur2);
            ICompteService.ajouterCompteUtilisateur(compte1);
            ICompteService.ajouterCompteUtilisateur(compte2);
			ICompteService.ajouterRoleToCompte(compte1,"USER");
            ICompteService.ajouterRoleToCompte(compte2,"ADMIN");
        };
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
