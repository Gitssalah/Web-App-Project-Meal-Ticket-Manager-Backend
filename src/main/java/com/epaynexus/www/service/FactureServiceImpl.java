package com.epaynexus.www.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.model.Commande;
import com.epaynexus.www.model.Facture;
import com.epaynexus.www.repository.FactureRepository;


@Service
public class FactureServiceImpl implements FactureService {
	private final FactureRepository factureRepository;
	
	public FactureServiceImpl (final FactureRepository factureRepository) {
		this.factureRepository = factureRepository;
	}
	
	@Override
	public Facture getFacture(Long reference) {
	   return factureRepository.findByCommande_Reference(reference)
	                .orElseThrow(() -> new NotFoundException("Facture non trouvée pour la commande avec la référence : " + reference));
	}

	@Transactional
	@Override
	public Facture genererFacture(Commande commande) {
		Facture facture = Facture.builder().commande(commande)
				.coutTotal(commande.getNombreSalaries()*25.0)
				.build();		
		return factureRepository.save(facture);
	}

}
