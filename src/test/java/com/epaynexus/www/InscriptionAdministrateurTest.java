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

import com.epaynexus.www.dto.InscriptionAdministrateurRequest;
import com.epaynexus.www.enumeration.TypeDeRoleEnum;
import com.epaynexus.www.model.Administrateur;
import com.epaynexus.www.repository.AdministrateurRepository;
import com.epaynexus.www.service.AdministrateurServiceImpl;

@SpringBootTest
class InscriptionAdministrateurTest {
	
	private static InscriptionAdministrateurRequest inscriptionAdministrateurRequest;
	@Mock
	private AdministrateurRepository administrateurRepository;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	@InjectMocks
	private AdministrateurServiceImpl administrateurServiceImpl;

	@BeforeAll
	static void init() {
		inscriptionAdministrateurRequest = new InscriptionAdministrateurRequest("admintest", "FAURE", "test.12@gmail.com", "Robin", "0638297409",
				TypeDeRoleEnum.ADMINISTRATEUR,"Reseau");
	}

	@Test
	void InscriptionAdministrateurTest() {
		when(passwordEncoder.encode(inscriptionAdministrateurRequest.mdp())).thenReturn("hashedPassword");
        when(administrateurRepository.save(any(Administrateur.class))).thenReturn(new Administrateur());
		Administrateur admin = administrateurServiceImpl.creerAdministrateur(inscriptionAdministrateurRequest);
		assertNotNull(admin, "admin instance should not be null");
	}
	
}
