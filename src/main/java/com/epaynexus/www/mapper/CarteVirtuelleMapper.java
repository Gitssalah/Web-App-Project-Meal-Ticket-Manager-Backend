package com.epaynexus.www.mapper;

import org.springframework.stereotype.Component;
import com.epaynexus.www.dto.CarteVirtuelleDTO;
import com.epaynexus.www.model.CarteVirtuelle;


@Component
public class CarteVirtuelleMapper {
	public CarteVirtuelleDTO mapToCarteVirtuelleDTO(CarteVirtuelle carteVirtuelle) {
		String numeroCarte = carteVirtuelle.getNumero();
		Boolean active = carteVirtuelle.getActive();
		String employeur = carteVirtuelle.getSalarie().getEmployeur().getNomEntreprise();
		String nom = carteVirtuelle.getSalarie().getNom();
		String prenom = carteVirtuelle.getSalarie().getPrenom();
		return new CarteVirtuelleDTO(nom, prenom, employeur, numeroCarte, active);

	}
}
