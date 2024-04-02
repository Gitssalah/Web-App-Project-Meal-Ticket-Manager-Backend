package com.epaynexus.www.model;

import java.util.List;
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
@Table(name = "administrateur")
@PrimaryKeyJoinColumn(name = "administrateur_id")
public class Administrateur extends Utilisateur {

	private String service;
	@OneToMany(mappedBy = "administrateur")
	private List<DemandeCreationCompte> demandeCreationComptes;
}