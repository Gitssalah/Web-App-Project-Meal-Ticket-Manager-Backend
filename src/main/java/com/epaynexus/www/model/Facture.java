package com.epaynexus.www.model;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table (name = "facture")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long identifiant;
	private double coutTotal;
	@OneToOne(mappedBy = "facture", cascade = CascadeType.PERSIST)
	private Commande commande;
}
