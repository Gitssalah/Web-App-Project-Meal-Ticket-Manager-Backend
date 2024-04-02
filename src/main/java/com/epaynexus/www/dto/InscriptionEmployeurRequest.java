package com.epaynexus.www.dto;

import com.epaynexus.www.enumeration.TypeDeRoleEnum;

public record InscriptionEmployeurRequest(
		String mdp, String nom, String email, String prenom, String telephone,
		TypeDeRoleEnum role, String nomEntreprise, String raisonSociale, String numSiret, String codePostal, String adresse,
		double effectif) {

}
