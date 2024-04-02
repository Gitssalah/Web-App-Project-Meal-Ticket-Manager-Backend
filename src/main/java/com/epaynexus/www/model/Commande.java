package com.epaynexus.www.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.epaynexus.www.enumeration.EtatCommandeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reference;
	private Date dateCreation;
	private EtatCommandeEnum etat;
	private int nombreSalaries;
	private Double plafondQuotidien;
	@JsonIgnore
	@OneToMany(mappedBy = "commande", cascade = CascadeType.PERSIST)
	private List<Salarie> salaries;
	@JsonIgnore
	@OneToOne
	private Facture facture;
	@ManyToOne
	private Employeur employeur;
}
