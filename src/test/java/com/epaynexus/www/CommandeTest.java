package com.epaynexus.www;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.epaynexus.www.dto.CreationCommandeRequest;
import com.epaynexus.www.dto.SalariesCommande;
import com.epaynexus.www.enumeration.EtatCommandeEnum;
import com.epaynexus.www.model.Commande;
import com.epaynexus.www.repository.CommandeRepository;
import com.epaynexus.www.service.CommandeService;

import ch.qos.logback.core.testUtil.RandomUtil;
import jakarta.transaction.Transactional;

@SpringBootTest
class CommandeTest {
	private static CreationCommandeRequest creationCommandeRequest;
	private static Commande commande;
	private CommandeService commandeService;
	private CommandeRepository commandeRepository;
	@Autowired
	public CommandeTest(CommandeService commandeService, CommandeRepository commandeRepository) {
		this.commandeService=commandeService;
		this.commandeRepository=commandeRepository;
	}

	@BeforeAll
	static void init() {
		creationCommandeRequest = new CreationCommandeRequest(1l, 38.5, List.of(new SalariesCommande("Sdiri",RandomUtil.getPositiveInt()+"sd.contact@gmail.com", "Dhia", "0645518442", "Chef Projet",45D),
				new SalariesCommande("Cavali",RandomUtil.getPositiveInt()+"cv.contact@gmail.com", "Sebastien", "07889505", "Technicien Sup",36D)));
	}

	@Test
	void creerCommandeTest() {
        commande= commandeService.creerCommande(creationCommandeRequest);
        assertEquals(commande.getNombreSalaries(), commande.getSalaries().size());
	}

	@After(value = "creerCommandeTest")
	void executerCommandeTest() {
		commandeService.executerCommande(commande.getReference());
		boolean checkActif=commande.getSalaries().stream().anyMatch(s->!s.getActif());
		assertTrue(checkActif&&commande.getEtat().equals(EtatCommandeEnum.TRAITEE));
	}

	@Test
	@Transactional
	void extraireToutesCommandesTest() {
		assertEquals(commandeRepository.count(), commandeService.extraireToutesCommandes().size());
	}

	@After(value = "creerCommandeTest")
	void chercherDetailsCommande() {
		Commande commandeDetails= commandeService.chercherDetailsCommande(commande.getReference());
		assertEquals(commande, commandeDetails);
	}
}
