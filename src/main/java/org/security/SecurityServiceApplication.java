package org.security;

import org.security.entities.Role;
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
            ICompteService.ajouterRole(new Role(null, "USER"));
            ICompteService.ajouterRole(new Role(null, "ADMIN"));
            ICompteService.ajouterUtilisateur(new Utilisateur(null, "Hicham", "1234", null));
            ICompteService.ajouterUtilisateur(new Utilisateur(null, "Ouahidi", "4321", null));
			ICompteService.ajouterRoleToUtilisateur("Hicham","USER");
            ICompteService.ajouterRoleToUtilisateur("Ouahidi","ADMIN");
        };
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
