package com.epaynexus.www.service;

import com.epaynexus.www.model.Commande;
import com.epaynexus.www.model.Facture;

public interface FactureService {
		Facture getFacture(Long reference);
		Facture genererFacture(Commande commande);
}
