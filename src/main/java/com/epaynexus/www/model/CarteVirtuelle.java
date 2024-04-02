package com.epaynexus.www.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class CarteVirtuelle implements Serializable{
	@Id
	private String numero;
	private Boolean active;
	private Date dateCreation;
	private Date dateExpiration;
	private Double solde;
	private Double plafondQuotidien;
	@JsonIgnore
	@OneToOne
	private Salarie salarie;
	@JsonIgnore
	@OneToMany(mappedBy = "carteVirtuelle", cascade = CascadeType.PERSIST)
	private transient List<Transaction> transactions;
}
