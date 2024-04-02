package com.epaynexus.www.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epaynexus.www.dto.DemandeCreationCompteRequest;
import com.epaynexus.www.dto.InscriptionCommercantRequest;
import com.epaynexus.www.dto.InscriptionEmployeurRequest;
import com.epaynexus.www.enumeration.DemandeCreationCompteActionEnum;
import com.epaynexus.www.enumeration.DemandeurEnum;
import com.epaynexus.www.enumeration.EtatDemandeCreationCompteEnum;
import com.epaynexus.www.enumeration.TypeDeRoleEnum;
import com.epaynexus.www.exception.EmailExistsException;
import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.model.Administrateur;
import com.epaynexus.www.model.DemandeCreationCompte;
import com.epaynexus.www.repository.DemandeCreationCompteRepository;

@Service
public class DemandeCreationCompteServiceImpl implements DemandeCreationCompteService {
	private final DemandeCreationCompteRepository demandeCreationCompteRepository;
	private final CommercantService commercantService;
	private final EmployeurService employeurService;
	private final UtilisateurService utilisateurService;
	private final AdministrateurService administrateurService;

	@Autowired // c'est useless car les nouvelles versions de spring peuvent traiter
				// l'autowiring automatiquement
	public DemandeCreationCompteServiceImpl(DemandeCreationCompteRepository demandeCCRepository,
			AdministrateurService administrateurService, UtilisateurService utilisateurService,
			EmployeurService employeurService, CommercantService commercantService) {
		this.demandeCreationCompteRepository = demandeCCRepository;
		this.utilisateurService = utilisateurService;
		this.commercantService = commercantService;
		this.employeurService = employeurService;
		this.administrateurService = administrateurService;
	}

	@Override
	public DemandeCreationCompte creerDemande(DemandeCreationCompteRequest demandeCreationCompteRequest) {
		if (!checkEmailAvailability(demandeCreationCompteRequest.email())) {
			throw new EmailExistsException("Cet email existe déjà");
		}
		DemandeCreationCompte demandeCreationCompte = DemandeCreationCompte.builder()
				.nom(demandeCreationCompteRequest.nom()).prenom(demandeCreationCompteRequest.prenom())
				.telephone(demandeCreationCompteRequest.telephone()).email(demandeCreationCompteRequest.email())
				.nomEntreprise(demandeCreationCompteRequest.nomEntreprise())
				.raisonSociale(demandeCreationCompteRequest.raisonSociale())
				.numSiret(demandeCreationCompteRequest.numSiret()).codePostal(demandeCreationCompteRequest.codePostal())
				.adresse(demandeCreationCompteRequest.adresse()).effectif(demandeCreationCompteRequest.effectif())
				.demandeur(demandeCreationCompteRequest.demandeur()).dateCreation(new Date())
				.etat(EtatDemandeCreationCompteEnum.ENCOURSDETRAITEMENT).build();
		return demandeCreationCompteRepository.save(demandeCreationCompte);
	}

	@Transactional
	@Override
	public void traiterDemande(Long identifiantAdmin, Long identifiantDemande, DemandeCreationCompteActionEnum action) {
		DemandeCreationCompte demandeCreationCompte = chercherDemandeCreationCompte(identifiantDemande);
		Administrateur administrateur = administrateurService.chercherAdministrateur(identifiantAdmin);
		if (administrateur != null)
			demandeCreationCompte.setAdministrateur(administrateur);
		if (action.equals(DemandeCreationCompteActionEnum.REJETER)) {
			demandeCreationCompte.setEtat(EtatDemandeCreationCompteEnum.REJETEE);
		}
		if (action.equals(DemandeCreationCompteActionEnum.VALIDER)) {
			demandeCreationCompte.setEtat(EtatDemandeCreationCompteEnum.VALIDEE);
			if (demandeCreationCompte.getDemandeur() != null) {
				if (demandeCreationCompte.getDemandeur().equals(DemandeurEnum.EMPLOYEUR)) {
					InscriptionEmployeurRequest inscriptionEmployeurRequest = new InscriptionEmployeurRequest(
							demandeCreationCompte.getTelephone(), demandeCreationCompte.getNom(),
							demandeCreationCompte.getEmail(), demandeCreationCompte.getPrenom(),
							demandeCreationCompte.getTelephone(), TypeDeRoleEnum.EMPLOYEUR,
							demandeCreationCompte.getNomEntreprise(), demandeCreationCompte.getRaisonSociale(),
							demandeCreationCompte.getNumSiret(), demandeCreationCompte.getCodePostal(),
							demandeCreationCompte.getAdresse(), demandeCreationCompte.getEffectif());
					employeurService.creerEmployeur(inscriptionEmployeurRequest);
				}
				if (demandeCreationCompte.getDemandeur().equals(DemandeurEnum.COMMERCANT)) {
					InscriptionCommercantRequest inscriptionCommercantRequest = new InscriptionCommercantRequest(
							demandeCreationCompte.getTelephone(), demandeCreationCompte.getNom(),
							demandeCreationCompte.getEmail(), demandeCreationCompte.getPrenom(),
							demandeCreationCompte.getTelephone(), TypeDeRoleEnum.COMMERCANT,
							demandeCreationCompte.getNomEntreprise(), demandeCreationCompte.getRaisonSociale(),
							demandeCreationCompte.getNumSiret(), demandeCreationCompte.getCodePostal(),
							demandeCreationCompte.getAdresse());
					commercantService.creerCommercant(inscriptionCommercantRequest);
				}
			}
		}
		demandeCreationCompteRepository.save(demandeCreationCompte);
	}

	@Override
	public List<DemandeCreationCompte> extraireDemandesCreationCompte() {
		return (List<DemandeCreationCompte>) demandeCreationCompteRepository.findAll();
	}

	@Override
	public DemandeCreationCompte chercherDemandeCreationCompte(Long identifiant) {
		DemandeCreationCompte demandeCreationCompte = demandeCreationCompteRepository.findById(identifiant)
				.orElse(null);
		if (demandeCreationCompte == null)
			throw new NotFoundException("Demande de création de compte non existante");
		return demandeCreationCompte;
	}

	@Override
	public boolean checkEmailAvailability(String email) {
		return utilisateurService.checkEmailAvailability(email)
				&& demandeCreationCompteRepository.findByEmail(email).isEmpty();
	}

}
