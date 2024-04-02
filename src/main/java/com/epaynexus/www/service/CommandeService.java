package com.epaynexus.www.service;

import java.util.List;
import com.epaynexus.www.dto.CommandeDTO;
import com.epaynexus.www.dto.CreationCommandeRequest;
import com.epaynexus.www.model.Commande;

public interface CommandeService {
	Commande creerCommande(CreationCommandeRequest creationCommandeRequest);

	List<CommandeDTO> extraireToutesCommandes();

	void executerCommande(Long reference);

	Commande chercherDetailsCommande(Long reference);
	
	List<CommandeDTO> extraireToutesCommandesByIdEmployeur(Long idEmployeur);
}
