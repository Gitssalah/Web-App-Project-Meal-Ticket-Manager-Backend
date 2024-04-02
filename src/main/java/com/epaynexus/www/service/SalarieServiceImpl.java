package com.epaynexus.www.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.epaynexus.www.dto.InscriptionSalarieRequest;
import com.epaynexus.www.enumeration.TypeDeRoleEnum;
import com.epaynexus.www.model.Salarie;
import com.epaynexus.www.repository.SalarieRepository;

@Service
public class SalarieServiceImpl implements SalarieService {
	private final SalarieRepository salarieRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public SalarieServiceImpl(BCryptPasswordEncoder passwordEncoder, SalarieRepository salarieRepository) {
		this.salarieRepository = salarieRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Salarie creerSalarie(InscriptionSalarieRequest inscritpionSalarieRequest) {
		Salarie existingSalarie = salarieRepository.findByEmail(inscritpionSalarieRequest.email());
		if (existingSalarie != null) {
			throw new IllegalStateException("Cet email est déjà utilisé par un autre salarie.");
		}
		Salarie salarie = new Salarie();
		String mdpCrypte = passwordEncoder.encode(inscritpionSalarieRequest.mdp());
		salarie.setNom(inscritpionSalarieRequest.nom());
		salarie.setMdp(mdpCrypte);
		salarie.setPrenom(inscritpionSalarieRequest.prenom());
		salarie.setRole(TypeDeRoleEnum.SALARIE);
		salarie.setActif(inscritpionSalarieRequest.actif());
		salarie.setTelephone(inscritpionSalarieRequest.telephone());
		salarie.setEmail(inscritpionSalarieRequest.email());
		salarie.setPoste(inscritpionSalarieRequest.poste());
		salarie.setForfaitJournalier(inscritpionSalarieRequest.forfaitJournalier());
		return salarieRepository.save(salarie);
	}

	public Map<String, String> consulterInfosPersos() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = userDetails.getUsername();
		Salarie salarie = salarieRepository.findByEmail(email);

		Map<String, String> infosSalarie = new HashMap<>();
		infosSalarie.put("email", salarie.getEmail());
		infosSalarie.put("nom", salarie.getNom());
		infosSalarie.put("prenom", salarie.getPrenom());
		infosSalarie.put("telephone", salarie.getTelephone());
		infosSalarie.put("poste", salarie.getPoste());
		infosSalarie.put("forfaitJournalier", String.valueOf(salarie.getForfaitJournalier()));

		return infosSalarie;
	}

}
