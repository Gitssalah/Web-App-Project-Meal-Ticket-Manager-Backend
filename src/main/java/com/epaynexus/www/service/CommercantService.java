package com.epaynexus.www.service;

import java.util.Map;

import com.epaynexus.www.dto.InscriptionCommercantRequest;
import com.epaynexus.www.model.Commercant;

public interface CommercantService {

	Commercant creerCommercant(InscriptionCommercantRequest inscritpionCommercantRequest);
	Commercant modifierComptePerso(String email, InscriptionCommercantRequest inscritpionCommercantRequest);
	Map<String,String> consulterInfosPersos();
	Commercant getCommercantById(Long commercantId);

}
