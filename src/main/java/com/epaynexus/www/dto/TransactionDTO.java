package com.epaynexus.www.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
	private Long identifiant;
    private String numCarte;
    private Double montant;
    private Date dateCreation;
    private Long carteVirtuelleDuSalarieId;
    private Long commercantId;
}
