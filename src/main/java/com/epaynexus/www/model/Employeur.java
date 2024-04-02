package com.epaynexus.www.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employeur")
@PrimaryKeyJoinColumn(name = "employeur_id")
public class Employeur extends Utilisateur {

	private String nomEntreprise;
	private String raisonSociale;
	private String numSiret;
	private String codePostal;
	private String adresse;
	private double effectif;
	@JsonIgnore
	@OneToMany(mappedBy = "employeur", cascade = CascadeType.PERSIST)
	private List<Commande> commandes;
	@JsonIgnore
	@OneToMany(mappedBy = "employeur", cascade = CascadeType.PERSIST)
	private List<Salarie>  salaries;

}