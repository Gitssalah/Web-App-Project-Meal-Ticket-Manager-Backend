package com.epaynexus.www.service;

import java.util.List;
import com.epaynexus.www.dto.DemandeCreationCompteRequest;
import com.epaynexus.www.enumeration.DemandeCreationCompteActionEnum;
import com.epaynexus.www.model.DemandeCreationCompte;

public interface DemandeCreationCompteService {
	DemandeCreationCompte creerDemande(DemandeCreationCompteRequest demandeCreationCompteRequest);

	void traiterDemande(Long identifiantAdmin, Long identifiantDemande, DemandeCreationCompteActionEnum action);

	List<DemandeCreationCompte> extraireDemandesCreationCompte();

	DemandeCreationCompte chercherDemandeCreationCompte(Long identifiant);
	
	boolean checkEmailAvailability(String email);
}
