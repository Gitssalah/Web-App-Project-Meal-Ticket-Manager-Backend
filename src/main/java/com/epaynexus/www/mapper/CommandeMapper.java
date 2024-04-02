package com.epaynexus.www.mapper;

import org.springframework.stereotype.Component;
import com.epaynexus.www.dto.CommandeDTO;
import com.epaynexus.www.model.Commande;
import com.epaynexus.www.model.Salarie;

@Component
public class CommandeMapper {
	public CommandeDTO mapToCommandeDTO(Commande commande) {
		CommandeDTO dto = new CommandeDTO();
		dto.setReference(commande.getReference());
		dto.setDateCreation(commande.getDateCreation());
		dto.setEtat(commande.getEtat());
		dto.setNombreSalaries(commande.getNombreSalaries());
		dto.setPlafondQuotidien(commande.getPlafondQuotidien());
		dto.setSalariesIds(commande.getSalaries().stream().map(Salarie::getId).toList());
		if (commande.getFacture() != null) {
			dto.setFactureId(commande.getFacture().getIdentifiant());
		}
		if (commande.getEmployeur() != null) {
			dto.setEmployeurId(commande.getEmployeur().getId());
			dto.setNomEmployeur(commande.getEmployeur().getNomEntreprise());
		}
		return dto;
	}
}
