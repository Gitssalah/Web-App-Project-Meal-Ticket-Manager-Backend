package com.epaynexus.www.model;

import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data //Combine @Getter, @Setter, @ToString, @EqualsAndHashCode et @RequiredArgsConstructor en une seule annotation
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long identifiant;
	private String numCarte;
	private Double montant;
	private Date dateCreation;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carte_virtuelle_numero", referencedColumnName = "numero")
    private CarteVirtuelle carteVirtuelle;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commercant_id")
	private Commercant commercant;
}
