package com.epaynexus.www;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import com.epaynexus.www.dto.DemandeCreationCompteRequest;
import com.epaynexus.www.enumeration.DemandeCreationCompteActionEnum;
import com.epaynexus.www.enumeration.DemandeurEnum;
import com.epaynexus.www.enumeration.EtatDemandeCreationCompteEnum;
import com.epaynexus.www.model.Administrateur;
import com.epaynexus.www.model.DemandeCreationCompte;
import com.epaynexus.www.repository.AdministrateurRepository;
import com.epaynexus.www.repository.DemandeCreationCompteRepository;
import com.epaynexus.www.service.AdministrateurServiceImpl;
import com.epaynexus.www.service.DemandeCreationCompteServiceImpl;
import com.epaynexus.www.service.UtilisateurService;

import ch.qos.logback.core.testUtil.RandomUtil;

@SpringBootTest
class DemandeCreationCompteTest {

	private DemandeCreationCompteRequest demandeCreationCompteRequest;
	@Mock
	private DemandeCreationCompteRepository demandeCreationCompteRepository;
	@Mock
	private AdministrateurRepository administrateurRepository;
	@InjectMocks
	private DemandeCreationCompteServiceImpl demandeCreationCompteService;
	@Mock
	private AdministrateurServiceImpl administrateurService;
	@Mock
	private UtilisateurService utilisateurService;

	@BeforeEach
	void setUp() {
		demandeCreationCompteRequest = new DemandeCreationCompteRequest("sdiri", "Dhia", "07541223",
				RandomUtil.getPositiveInt() + "des@hotmail.com", "DS Comp", "RaisSoc", "1512SI9", "69100",
				"20 Rue de Albert Einstein", 50, DemandeurEnum.EMPLOYEUR);
	}

	@Test
	void creerDemandeTest() {
		when(utilisateurService.checkEmailAvailability(anyString())).thenReturn(true);
		when(demandeCreationCompteRepository.save(any(DemandeCreationCompte.class)))
				.thenReturn(new DemandeCreationCompte());
		DemandeCreationCompte demande = demandeCreationCompteService.creerDemande(demandeCreationCompteRequest);
		assertNotNull(demande, "demande instance should not be null");
	}

	@Test
	void validerDemandeTest() {
		when(utilisateurService.checkEmailAvailability(anyString())).thenReturn(true);
		when(demandeCreationCompteRepository.save(any(DemandeCreationCompte.class)))
				.thenReturn(new DemandeCreationCompte());
		DemandeCreationCompte demande = demandeCreationCompteService.creerDemande(demandeCreationCompteRequest);
		when(demandeCreationCompteRepository.findById(anyLong())).thenReturn(Optional.of(demande));
		Administrateur administrateur = new Administrateur();
		doReturn(Optional.of(administrateur)).when(administrateurRepository).findById(1L);
		demandeCreationCompteService.traiterDemande(1L, 1L, DemandeCreationCompteActionEnum.VALIDER);
		verify(demandeCreationCompteRepository, times(1)).save(demande);
		assertEquals(EtatDemandeCreationCompteEnum.VALIDEE, demande.getEtat());
	}

	@Test
	void rejeterDemandeTest() {
		when(utilisateurService.checkEmailAvailability(anyString())).thenReturn(true);
		when(demandeCreationCompteRepository.save(any(DemandeCreationCompte.class)))
				.thenReturn(new DemandeCreationCompte());
		DemandeCreationCompte demande = demandeCreationCompteService.creerDemande(demandeCreationCompteRequest);
		when(demandeCreationCompteRepository.findById(anyLong())).thenReturn(Optional.of(demande));
		Administrateur administrateur = new Administrateur();
		doReturn(Optional.of(administrateur)).when(administrateurRepository).findById(anyLong());
		demandeCreationCompteService.traiterDemande(1L, 1L, DemandeCreationCompteActionEnum.REJETER);
		verify(demandeCreationCompteRepository, times(1)).save(demande);
		assertEquals(EtatDemandeCreationCompteEnum.REJETEE, demande.getEtat());
	}

	@Test
	void extraireToutesDemandesTest() {
		List<DemandeCreationCompte> demandesMock = List.of(
				new DemandeCreationCompte(null, "Cavali", "Marie", "049879876", "mc@hotmail.com", "MC Gatsro", "RS",
						"74512SR", "69100", "15 Av Alb Eins", 0, DemandeurEnum.COMMERCANT,
						EtatDemandeCreationCompteEnum.ENCOURSDETRAITEMENT, new Date(), null),
				new DemandeCreationCompte(null, "Pablo", "Sebastien", "049879876", "SP@hotmail.com", "SP Company", "RS",
						"74510SR", "69100", "26 Av Alb Eins", 180, DemandeurEnum.EMPLOYEUR,
						EtatDemandeCreationCompteEnum.VALIDEE, new Date(), null));
		when(demandeCreationCompteRepository.findAll()).thenReturn(demandesMock);
		List<DemandeCreationCompte> demandes = demandeCreationCompteService.extraireDemandesCreationCompte();
		verify(demandeCreationCompteRepository, times(1)).findAll();
		assertEquals(demandesMock, demandes);
	}

	@Test
	void chercherDetailsDemande() {
		DemandeCreationCompte demandeMock = new DemandeCreationCompte();
		when(demandeCreationCompteRepository.findById(1L)).thenReturn(Optional.of(demandeMock));
		DemandeCreationCompte demande = demandeCreationCompteService.chercherDemandeCreationCompte(1L);
		verify(demandeCreationCompteRepository, times(1)).findById(1L);
		assertEquals(demandeMock, demande);
	}
}
