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

import com.epaynexus.www.dto.InscriptionCommercantRequest;
import com.epaynexus.www.enumeration.TypeDeRoleEnum;
import com.epaynexus.www.model.Commercant;
import com.epaynexus.www.repository.CommercantRepository;
import com.epaynexus.www.service.CommercantServiceImpl;

@SpringBootTest
class InscriptionCommercantTest {
	
	private static InscriptionCommercantRequest inscriptionCommercantRequest;
	@Mock
	private CommercantRepository commercantRepository;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	@InjectMocks
	private CommercantServiceImpl commercantServiceImpl;

	@BeforeAll
	static void init() {
		inscriptionCommercantRequest = new InscriptionCommercantRequest(
		    "commercant1",
		    "PROFANI",
		    "commercant1@gmail.com",
		    "Martin",
		    "0654879548",
		    TypeDeRoleEnum.COMMERCANT,
		    "Petit Casino",
		    "Raison Sociale test",
		    "156423157865",
		    "69250",
		    "10 rue des Potiers"
		   );}

	@Test
	void InscriptionCommercantTest() {
		when(passwordEncoder.encode(inscriptionCommercantRequest.mdp())).thenReturn("hashedPassword");
        when(commercantRepository.save(any(Commercant.class))).thenReturn(new Commercant());
		Commercant commercant = commercantServiceImpl.creerCommercant(inscriptionCommercantRequest);
		assertNotNull(commercant, "commercant instance should not be null");
	}
	
}
