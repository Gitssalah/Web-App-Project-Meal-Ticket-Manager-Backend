package com.epaynexus.www.model;

import java.io.Serializable;
import java.util.Date;
import com.epaynexus.www.enumeration.EtatDemandeCreationCompteEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.epaynexus.www.enumeration.DemandeurEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data //Combine @Getter, @Setter, @ToString, @EqualsAndHashCode et @RequiredArgsConstructor en une seule annotation
@NoArgsConstructor
@AllArgsConstructor
@Builder //utilisée pour générer automatiquement un pattern builder pour vos classes Java (voir la classe serviceImpl: méthode creerDemande())
public class DemandeCreationCompte implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long identifiant;
	private String nom;
	private String prenom;
	private String telephone;
	private String email;
	private String nomEntreprise;
	private String raisonSociale;
	private String numSiret;
	private String codePostal;
	private String adresse;
	private int effectif;
	private DemandeurEnum demandeur;
	private EtatDemandeCreationCompteEnum etat;
	private Date dateCreation;
	@JsonIgnore
	@ManyToOne
	private Administrateur administrateur;
}
