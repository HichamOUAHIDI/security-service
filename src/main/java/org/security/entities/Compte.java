package org.security.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifiant;

    private String pseudonyme;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private String modePasse;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="id_utilisateur")
    private Utilisateur utilisateur;
}
