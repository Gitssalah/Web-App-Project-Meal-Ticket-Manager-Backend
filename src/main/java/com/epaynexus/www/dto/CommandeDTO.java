package com.epaynexus.www.dto;

import java.util.Date;
import java.util.List;
import com.epaynexus.www.enumeration.EtatCommandeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeDTO {
	private Long reference;
	private Date dateCreation;
	private EtatCommandeEnum etat;
	private int nombreSalaries;
	private Double plafondQuotidien;
	private List<Long> salariesIds;
	private Long factureId;
	private Long employeurId;
	private String nomEmployeur;
}