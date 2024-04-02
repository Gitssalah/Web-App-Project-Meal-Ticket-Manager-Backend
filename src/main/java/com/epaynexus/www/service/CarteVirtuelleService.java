package com.epaynexus.www.service;

import java.util.List;

import com.epaynexus.www.dto.CarteVirtuelleDTO;
import com.epaynexus.www.model.CarteVirtuelle;

import net.minidev.json.JSONObject;

public interface CarteVirtuelleService {
	void activerCarteVirtuelle(String numero);
	void desactiverCarteVirtuelle(String numero);
	CarteVirtuelle getCarteVirtuelleByNum(String numero);
	List<CarteVirtuelleDTO> extraireToutesCartes();
	Boolean verifierCarte(String numCarte, Double montant, Double depenseJournalier);
	void modifierSolde(String numCarte, Double montant);
	void alimenterCartesJOB();
	JSONObject getSoldeCarteVirtuelleBySalarieId(Long salarieId);
	Double calculerSoldeJournalier(Long salarieId);
	CarteVirtuelle getCarteVirtuelleBySalarieId(Long salarieId);
}
