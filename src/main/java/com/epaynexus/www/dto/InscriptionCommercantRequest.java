package com.epaynexus.www.dto;

import com.epaynexus.www.enumeration.TypeDeRoleEnum;

public record InscriptionCommercantRequest(
		String mdp, String nom, String email, String prenom, String telephone,
		TypeDeRoleEnum role, String nomEntreprise, String raisonSociale, String numSiret, String codePostal, String adresse
		) {

}
