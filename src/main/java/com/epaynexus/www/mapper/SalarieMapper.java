package com.epaynexus.www.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.epaynexus.www.dto.SalariesCommande;
import com.epaynexus.www.enumeration.TypeDeRoleEnum;
import com.epaynexus.www.model.Commande;
import com.epaynexus.www.model.Salarie;

@Component
public class SalarieMapper {

	public Salarie mapToSalarieInactif(SalariesCommande salariesCommande, Commande commande,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		Salarie salarie = new Salarie();
		salarie.setNom(salariesCommande.nom());
		salarie.setPrenom(salariesCommande.prenom());
		salarie.setEmail(salariesCommande.email());
		salarie.setTelephone(salariesCommande.telephone());
		salarie.setActif(false);
		salarie.setForfaitJournalier(salariesCommande.forfaitJournalier());
		salarie.setPoste(salariesCommande.poste());
		salarie.setCommande(commande);
		salarie.setMdp(bCryptPasswordEncoder.encode(salariesCommande.telephone()));
		salarie.setRole(TypeDeRoleEnum.SALARIE);
		salarie.setEmployeur(commande.getEmployeur());
		return salarie;
	}
}
