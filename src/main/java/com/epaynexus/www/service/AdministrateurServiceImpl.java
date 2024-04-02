package com.epaynexus.www.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.epaynexus.www.dto.InscriptionAdministrateurRequest;
import com.epaynexus.www.dto.InscriptionCommercantRequest;
import com.epaynexus.www.dto.InscriptionEmployeurRequest;
import com.epaynexus.www.dto.InscriptionSalarieRequest;
import com.epaynexus.www.enumeration.TypeDeRoleEnum;
import com.epaynexus.www.model.Administrateur;
import com.epaynexus.www.model.Commercant;
import com.epaynexus.www.model.Employeur;
import com.epaynexus.www.model.Salarie;
import com.epaynexus.www.model.Utilisateur;
import com.epaynexus.www.repository.AdministrateurRepository;
import com.epaynexus.www.repository.CommercantRepository;
import com.epaynexus.www.repository.EmployeurRepository;
import com.epaynexus.www.repository.SalarieRepository;
import com.epaynexus.www.repository.UtilisateurRepository;

@Service
public class AdministrateurServiceImpl implements AdministrateurService {
	private final BCryptPasswordEncoder passwordEncoder;
	private final AdministrateurRepository administrateurRepository;
	private final CommercantRepository commercantRepository;
	private final SalarieRepository salarieRepository;
	private final EmployeurRepository employeurRepository;
	private final UtilisateurRepository utilisateurRepository;

	public AdministrateurServiceImpl(BCryptPasswordEncoder passwordEncoder,
			AdministrateurRepository administrateurRepository, CommercantRepository commercantRepository,
			SalarieRepository salarieRepository, EmployeurRepository employeurRepository,
			UtilisateurRepository utilisateurRepository) {
		this.administrateurRepository = administrateurRepository;
		this.passwordEncoder = passwordEncoder;
		this.commercantRepository = commercantRepository;
		this.salarieRepository = salarieRepository;
		this.employeurRepository = employeurRepository;
		this.utilisateurRepository = utilisateurRepository;
	}

	@Override
	public Administrateur creerAdministrateur(InscriptionAdministrateurRequest inscritpionAdministrateurRequest) {
		Administrateur existingAdministrateur = administrateurRepository
				.findByEmail(inscritpionAdministrateurRequest.email());
		if (existingAdministrateur != null) {
			throw new IllegalStateException("Cet email est déjà utilisé par un autre administrateur.");
		}
		Administrateur administrateur = new Administrateur();
		String mdpCrypte = passwordEncoder.encode(inscritpionAdministrateurRequest.mdp());
		administrateur.setNom(inscritpionAdministrateurRequest.nom());
		administrateur.setMdp(mdpCrypte);
		administrateur.setPrenom(inscritpionAdministrateurRequest.prenom());
		administrateur.setRole(TypeDeRoleEnum.ADMINISTRATEUR);
		administrateur.setTelephone(inscritpionAdministrateurRequest.telephone());
		administrateur.setEmail(inscritpionAdministrateurRequest.email());
		administrateur.setService(inscritpionAdministrateurRequest.service());

		return administrateurRepository.save(administrateur);

	}

	@Override
	public Administrateur modifierCompteAdmin(String email,
			InscriptionAdministrateurRequest inscritpionAdministrateurRequest) {
		Administrateur administrateur = administrateurRepository.findByEmail(email);
		String newPassword = inscritpionAdministrateurRequest.mdp();
		if (newPassword != null) {
			String mdpCrypte = passwordEncoder.encode(inscritpionAdministrateurRequest.mdp());
			administrateur.setMdp(mdpCrypte);
		}
		administrateur.setNom(inscritpionAdministrateurRequest.nom());
		administrateur.setPrenom(inscritpionAdministrateurRequest.prenom());
		administrateur.setTelephone(inscritpionAdministrateurRequest.telephone());
		administrateur.setEmail(inscritpionAdministrateurRequest.email());
		administrateur.setService(inscritpionAdministrateurRequest.service());

		return administrateurRepository.save(administrateur);
	}

	public Administrateur modifierComptePerso(String email,
			InscriptionAdministrateurRequest inscritpionAdministrateurRequest) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailConnecte = userDetails.getUsername();

		if (!emailConnecte.equals(email)) {
			throw new IllegalArgumentException("Vous n'êtes pas autorisé à modifier ce compte");
		}

		Administrateur administrateur = administrateurRepository.findByEmail(email);
		String newPassword = inscritpionAdministrateurRequest.mdp();
		if (newPassword != null) {
			String mdpCrypte = passwordEncoder.encode(inscritpionAdministrateurRequest.mdp());
			administrateur.setMdp(mdpCrypte);
		}
		administrateur.setNom(inscritpionAdministrateurRequest.nom());
		administrateur.setPrenom(inscritpionAdministrateurRequest.prenom());
		administrateur.setTelephone(inscritpionAdministrateurRequest.telephone());
		administrateur.setEmail(inscritpionAdministrateurRequest.email());
		administrateur.setService(inscritpionAdministrateurRequest.service());

		return administrateurRepository.save(administrateur);
	}

	@Override
	public Commercant modifierCompteCommercant(String email,
			InscriptionCommercantRequest inscritpionCommercantRequest) {
		Commercant commercant = commercantRepository.findByEmail(email);
		String newPassword = inscritpionCommercantRequest.mdp();
		if (newPassword != null) {
			String mdpCrypte = passwordEncoder.encode(inscritpionCommercantRequest.mdp());
			commercant.setMdp(mdpCrypte);
		}
		commercant.setNom(inscritpionCommercantRequest.nom());
		commercant.setPrenom(inscritpionCommercantRequest.prenom());
		commercant.setTelephone(inscritpionCommercantRequest.telephone());
		commercant.setEmail(inscritpionCommercantRequest.email());
		commercant.setNomEntreprise(inscritpionCommercantRequest.nomEntreprise());
		commercant.setRaisonSociale(inscritpionCommercantRequest.raisonSociale());
		commercant.setNumSiret(inscritpionCommercantRequest.numSiret());
		commercant.setCodePostal(inscritpionCommercantRequest.codePostal());
		commercant.setAdresse(inscritpionCommercantRequest.adresse());

		return commercantRepository.save(commercant);
	}

	@Override
	public Salarie modifierCompteSalarie(String email, InscriptionSalarieRequest inscritpionSalarieRequest) {
		Salarie salarie = salarieRepository.findByEmail(email);
		String newPassword = inscritpionSalarieRequest.mdp();
		if (newPassword != null) {
			String mdpCrypte = passwordEncoder.encode(inscritpionSalarieRequest.mdp());
			salarie.setMdp(mdpCrypte);
		}
		salarie.setNom(inscritpionSalarieRequest.nom());
		salarie.setPrenom(inscritpionSalarieRequest.prenom());
		salarie.setActif(inscritpionSalarieRequest.actif());
		salarie.setTelephone(inscritpionSalarieRequest.telephone());
		salarie.setEmail(inscritpionSalarieRequest.email());
		salarie.setPoste(inscritpionSalarieRequest.poste());
		salarie.setForfaitJournalier(inscritpionSalarieRequest.forfaitJournalier());
		return salarieRepository.save(salarie);
	}

	@Override
	public Employeur modifierCompteEmployeur(String email, InscriptionEmployeurRequest inscritpionEmployeurRequest) {
		Employeur employeur = employeurRepository.findByEmail(email);
		String newPassword = inscritpionEmployeurRequest.mdp();
		if (newPassword != null) {
			String mdpCrypte = passwordEncoder.encode(inscritpionEmployeurRequest.mdp());
			employeur.setMdp(mdpCrypte);
		}
		employeur.setNom(inscritpionEmployeurRequest.nom());
		employeur.setPrenom(inscritpionEmployeurRequest.prenom());
		employeur.setTelephone(inscritpionEmployeurRequest.telephone());
		employeur.setEmail(inscritpionEmployeurRequest.email());
		employeur.setNomEntreprise(inscritpionEmployeurRequest.nomEntreprise());
		employeur.setRaisonSociale(inscritpionEmployeurRequest.raisonSociale());
		employeur.setNumSiret(inscritpionEmployeurRequest.numSiret());
		employeur.setCodePostal(inscritpionEmployeurRequest.codePostal());
		employeur.setAdresse(inscritpionEmployeurRequest.adresse());
		employeur.setEffectif(inscritpionEmployeurRequest.effectif());

		return employeurRepository.save(employeur);
	}

	@Override
	public Administrateur chercherAdministrateur(Long id) {
		return administrateurRepository.findById(id).orElse(null);
	}

	@Override
	public List<Utilisateur> extraireComptes() {
		return (List<Utilisateur>) utilisateurRepository.findAll();
	}

	public Map<String, String> consulterInfosPersos() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		Administrateur administrateur = administrateurRepository.findByEmail(email);

		Map<String, String> infosAdmin = new HashMap<>();
		infosAdmin.put("email", administrateur.getEmail());
		infosAdmin.put("nom", administrateur.getNom());
		infosAdmin.put("prenom", administrateur.getPrenom());
		infosAdmin.put("telephone", administrateur.getTelephone());
		infosAdmin.put("service", administrateur.getService());

		return infosAdmin;
	}

}
