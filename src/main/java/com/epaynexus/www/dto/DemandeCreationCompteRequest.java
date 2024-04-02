package com.epaynexus.www.dto;

import com.epaynexus.www.enumeration.DemandeurEnum;

//Les records permettent de définir de manière concise des classes qui
//servent principalement de conteneurs de données (A partir de JAVA 16)
public record DemandeCreationCompteRequest(String nom, String prenom, String telephone, String email,
		String nomEntreprise, String raisonSociale, String numSiret, String codePostal, String adresse, int effectif,
		DemandeurEnum demandeur) {

}
