package com.epaynexus.www.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.epaynexus.www.dto.InscriptionEmployeurRequest;
import com.epaynexus.www.dto.InscriptionSalarieRequest;
import com.epaynexus.www.enumeration.TypeDeRoleEnum;
import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.model.Commercant;
import com.epaynexus.www.model.Employeur;
import com.epaynexus.www.model.Salarie;
import com.epaynexus.www.repository.EmployeurRepository;
import com.epaynexus.www.repository.SalarieRepository;

@Service
public class EmployeurServiceImpl implements EmployeurService {
	private final EmployeurRepository employeurRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final SalarieRepository salarieRepository;

	public EmployeurServiceImpl(BCryptPasswordEncoder passwordEncoder, EmployeurRepository employeurRepository,
			SalarieRepository salarieRepository) {
		this.employeurRepository = employeurRepository;
		this.passwordEncoder = passwordEncoder;
		this.salarieRepository = salarieRepository;
	}

	@Override
	public Employeur creerEmployeur(InscriptionEmployeurRequest inscritpionEmployeurRequest) {
		if (employeurRepository.findByEmail(inscritpionEmployeurRequest.email()) != null) {
			throw new IllegalStateException("Cet email est déjà utilisé par un autre employeur.");
		}
		Employeur employeur = new Employeur();
		String mdpCrypte = passwordEncoder.encode(inscritpionEmployeurRequest.mdp());
		employeur.setNom(inscritpionEmployeurRequest.nom());
		employeur.setMdp(mdpCrypte);
		employeur.setPrenom(inscritpionEmployeurRequest.prenom());
		employeur.setRole(TypeDeRoleEnum.EMPLOYEUR);
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

	public Employeur modifierComptePerso(String email, InscriptionEmployeurRequest inscritpionEmployeurRequest) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String emailConnecte = userDetails.getUsername();

		if (!emailConnecte.equals(email)) {
			throw new IllegalArgumentException("Vous n'êtes pas autorisé à modifier ce compte");
		}

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
	public Employeur chercherEmployeur(Long id) {
		return employeurRepository.findById(id).orElseThrow(() -> new NotFoundException("Employeur non existant"));
	}

	@Override
	public List<Salarie> extraireComptesSalaries() {
		return (List<Salarie>) salarieRepository.findAll();
	}
	
	public Map<String, String> consulterInfosPersos() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		Employeur employeur = employeurRepository.findByEmail(email);

		Map<String, String> infosAdmin = new HashMap<>();
		infosAdmin.put("email", employeur.getEmail());
		infosAdmin.put("nom", employeur.getNom());
		infosAdmin.put("prenom", employeur.getPrenom());
		infosAdmin.put("telephone", employeur.getTelephone());
		infosAdmin.put("nomEntreprise", employeur.getNomEntreprise());
		infosAdmin.put("raisonSociale", employeur.getRaisonSociale());
		infosAdmin.put("numSiret", employeur.getNumSiret());
		infosAdmin.put("codePostal", employeur.getCodePostal());
		infosAdmin.put("adresse", employeur.getAdresse());
	    String effectifString = Double.toString(employeur.getEffectif());
	    infosAdmin.put("effectif", effectifString);



		return infosAdmin;
	}

}
