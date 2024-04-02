package com.epaynexus.www;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.epaynexus.www.dto.InscriptionEmployeurRequest;
import com.epaynexus.www.enumeration.TypeDeRoleEnum;
import com.epaynexus.www.model.Employeur;
import com.epaynexus.www.repository.EmployeurRepository;
import com.epaynexus.www.service.EmployeurServiceImpl;

@SpringBootTest
class InscriptionEmployeurTest {
	
	private static InscriptionEmployeurRequest inscriptionEmployeurRequest;
	@Mock
	private EmployeurRepository employeurRepository;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	@InjectMocks
	private EmployeurServiceImpl employeurServiceImpl;

	@BeforeAll
	static void init() {
		inscriptionEmployeurRequest = new InscriptionEmployeurRequest(
		    "employeur1",
		    "PROFANI",
		    "employeur1@gmail.com",
		    "Martin",
		    "0654879548",
		    TypeDeRoleEnum.EMPLOYEUR,
		    "Petit Casino",
		    "Raison Sociale test",
		    "156423157865",
		    "69250",
		    "10 rue des Potiers",
		    150
		   );}

	@Test
	void InscriptionEmployeurTest() {
		when(passwordEncoder.encode(inscriptionEmployeurRequest.mdp())).thenReturn("hashedPassword");
        when(employeurRepository.save(any(Employeur.class))).thenReturn(new Employeur());
		Employeur employeur = employeurServiceImpl.creerEmployeur(inscriptionEmployeurRequest);
		assertNotNull(employeur, "employeur instance should not be null");
	}
	
}
