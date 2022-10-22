package org.security.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_utilisateur;

    private String nom;

    private String prenom;

    private String email;

    private String telephone;

    private String role;

    @OneToMany(
            mappedBy = "utilisateur",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Compte> comptes = new ArrayList<>();

}
