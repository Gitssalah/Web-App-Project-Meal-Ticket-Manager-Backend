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

import com.epaynexus.www.dto.InscriptionSalarieRequest;
import com.epaynexus.www.enumeration.TypeDeRoleEnum;
import com.epaynexus.www.model.Salarie;
import com.epaynexus.www.repository.SalarieRepository;
import com.epaynexus.www.service.SalarieServiceImpl;

@SpringBootTest
class InscriptionSalarieTest {
	
	private static InscriptionSalarieRequest inscriptionSalarieRequest;
	@Mock
	private SalarieRepository salarieRepository;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	@InjectMocks
	private SalarieServiceImpl salarieServiceImpl;

	@BeforeAll
	static void init() {
		inscriptionSalarieRequest = new InscriptionSalarieRequest("salarietest", "FAURE", "test.12@gmail.com", "Robin", "0638297409",
				TypeDeRoleEnum.SALARIE,"RH",15.0,true);
	}

	@Test
	void InscriptionSalarieTest() {
		when(passwordEncoder.encode(inscriptionSalarieRequest.mdp())).thenReturn("hashedPassword");
        when(salarieRepository.save(any(Salarie.class))).thenReturn(new Salarie());
		Salarie admin = salarieServiceImpl.creerSalarie(inscriptionSalarieRequest);
		assertNotNull(admin, "admin instance should not be null");
	}
	
}
