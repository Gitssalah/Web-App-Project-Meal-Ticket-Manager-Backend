package com.epaynexus.www.service;

import java.util.List;
import java.util.Map;

import com.epaynexus.www.dto.InscriptionAdministrateurRequest;
import com.epaynexus.www.dto.InscriptionCommercantRequest;
import com.epaynexus.www.dto.InscriptionEmployeurRequest;
import com.epaynexus.www.dto.InscriptionSalarieRequest;
import com.epaynexus.www.model.Administrateur;
import com.epaynexus.www.model.Commercant;
import com.epaynexus.www.model.Employeur;
import com.epaynexus.www.model.Salarie;
import com.epaynexus.www.model.Utilisateur;

public interface AdministrateurService {

	Administrateur creerAdministrateur(InscriptionAdministrateurRequest inscritpionAdministrateurRequest);
	Administrateur modifierCompteAdmin(String email, InscriptionAdministrateurRequest inscritpionAdministrateurRequest);
	Administrateur modifierComptePerso(String email, InscriptionAdministrateurRequest inscritpionAdministrateurRequest);
	Commercant modifierCompteCommercant(String email, InscriptionCommercantRequest inscritpionCommercantRequest);
	Salarie modifierCompteSalarie(String email, InscriptionSalarieRequest inscritpionSalarieRequest);
	Employeur modifierCompteEmployeur(String email, InscriptionEmployeurRequest inscritpionEmployeurRequest);
	Administrateur chercherAdministrateur(Long id);
	List<Utilisateur> extraireComptes();
	Map<String,String> consulterInfosPersos();
}
