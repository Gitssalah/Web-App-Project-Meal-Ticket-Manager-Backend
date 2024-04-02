package com.epaynexus.www.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.epaynexus.www.dto.InscriptionCommercantRequest;
import com.epaynexus.www.enumeration.TypeDeRoleEnum;
import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.model.Administrateur;
import com.epaynexus.www.model.Commercant;
import com.epaynexus.www.repository.CommercantRepository;


@Service
public class CommercantServiceImpl implements CommercantService {
	private final CommercantRepository commercantRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
    public CommercantServiceImpl(BCryptPasswordEncoder passwordEncoder,CommercantRepository commercantRepository) {
        this.commercantRepository = commercantRepository;
        this.passwordEncoder = passwordEncoder;
    }
	
	@Override
	public Commercant creerCommercant(InscriptionCommercantRequest inscritpionCommercantRequest) {
        if (commercantRepository.findByEmail(inscritpionCommercantRequest.email()) != null) {
            throw new IllegalStateException("Cet email est déjà utilisé par un autre commercant.");
        }
		Commercant commercant = new Commercant();
		String mdpCrypte = passwordEncoder.encode(inscritpionCommercantRequest.mdp());
		commercant.setNom(inscritpionCommercantRequest.nom());
        commercant.setMdp(mdpCrypte);
        commercant.setPrenom(inscritpionCommercantRequest.prenom());
        commercant.setRole(TypeDeRoleEnum.COMMERCANT);
        commercant.setTelephone(inscritpionCommercantRequest.telephone());
        commercant.setEmail(inscritpionCommercantRequest.email());
        commercant.setNomEntreprise(inscritpionCommercantRequest.nomEntreprise());
		commercant.setRaisonSociale(inscritpionCommercantRequest.raisonSociale());
		commercant.setNumSiret(inscritpionCommercantRequest.numSiret());
		commercant.setCodePostal(inscritpionCommercantRequest.codePostal());
		commercant.setAdresse(inscritpionCommercantRequest.adresse());
		
		return commercantRepository.save(commercant);
		
	}
	
	public Commercant modifierComptePerso(String email,InscriptionCommercantRequest inscritpionCommercantRequest) {
		
		 UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    String emailCommercantConnecte = userDetails.getUsername();

		    if (!emailCommercantConnecte.equals(email)) {
		        throw new IllegalArgumentException("Vous n'êtes pas autorisé à modifier ce compte");
		    }
		Commercant commercant = commercantRepository.findByEmail(email);
		String newPassword = inscritpionCommercantRequest.mdp();
		if(newPassword != null) {
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

	public Map<String, String> consulterInfosPersos() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		Commercant commercant = commercantRepository.findByEmail(email);

		Map<String, String> infosAdmin = new HashMap<>();
		infosAdmin.put("email", commercant.getEmail());
		infosAdmin.put("nom", commercant.getNom());
		infosAdmin.put("prenom", commercant.getPrenom());
		infosAdmin.put("telephone", commercant.getTelephone());
		infosAdmin.put("nomEntreprise", commercant.getNomEntreprise());
		infosAdmin.put("raisonSociale", commercant.getRaisonSociale());
		infosAdmin.put("numSiret", commercant.getNumSiret());
		infosAdmin.put("codePostal", commercant.getCodePostal());
		infosAdmin.put("adresse", commercant.getAdresse());


		return infosAdmin;
	}

	@Override
	public Commercant getCommercantById(Long commercantId) {
		return commercantRepository.findById(commercantId)
				.orElseThrow(() -> new NotFoundException("Commercant inexistant: " + commercantId));
    }
	

}
