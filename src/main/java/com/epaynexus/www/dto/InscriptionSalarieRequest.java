package com.epaynexus.www.dto;

import com.epaynexus.www.enumeration.TypeDeRoleEnum;

public record InscriptionSalarieRequest(String mdp, String nom, String email, String prenom, String telephone,
		TypeDeRoleEnum role,String poste, Double forfaitJournalier, boolean actif ) {

}
