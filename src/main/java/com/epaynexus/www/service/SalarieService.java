package com.epaynexus.www.service;

import java.util.Map;

import com.epaynexus.www.dto.InscriptionSalarieRequest;
import com.epaynexus.www.model.Salarie;

public interface SalarieService {
	Salarie creerSalarie(InscriptionSalarieRequest inscritpionSalarieRequest);
	Map<String,String> consulterInfosPersos();
}
