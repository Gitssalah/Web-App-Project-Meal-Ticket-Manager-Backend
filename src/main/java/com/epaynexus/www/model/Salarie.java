package com.epaynexus.www.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "salarie")
@PrimaryKeyJoinColumn(name = "salarie_id")
public class Salarie extends Utilisateur {

	private String poste;
	private Double forfaitJournalier;
	private Boolean actif;
	@OneToOne(mappedBy = "salarie", cascade = CascadeType.PERSIST)
	private CarteVirtuelle carteVirtuelle;
	@ManyToOne
	private Commande commande;
	@ManyToOne
	private Employeur employeur;
	
	public void setActif(boolean actif) {
	    this.actif = actif;
	}

}
