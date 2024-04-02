package com.epaynexus.www.service;

import java.util.List;
import java.util.Map;

import com.epaynexus.www.dto.InscriptionEmployeurRequest;
import com.epaynexus.www.dto.InscriptionSalarieRequest;
import com.epaynexus.www.model.Employeur;
import com.epaynexus.www.model.Salarie;

public interface EmployeurService {

	Employeur modifierComptePerso(String email, InscriptionEmployeurRequest inscritpionEmployeurRequest);

	Employeur creerEmployeur(InscriptionEmployeurRequest inscritpionEmployeurRequest);

	Salarie modifierCompteSalarie(String email, InscriptionSalarieRequest inscritpionSalarieRequest);

	Employeur chercherEmployeur(Long id);

	List<Salarie> extraireComptesSalaries();
	Map<String,String> consulterInfosPersos();
}
