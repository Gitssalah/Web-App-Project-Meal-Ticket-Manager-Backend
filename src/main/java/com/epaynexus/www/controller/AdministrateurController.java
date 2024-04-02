package com.epaynexus.www.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epaynexus.www.dto.InscriptionAdministrateurRequest;
import com.epaynexus.www.dto.InscriptionCommercantRequest;
import com.epaynexus.www.dto.InscriptionEmployeurRequest;
import com.epaynexus.www.dto.InscriptionSalarieRequest;
import com.epaynexus.www.service.AdministrateurService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "API/Administrateur")
public class AdministrateurController {

	private AdministrateurService administrateurService;
	private static final String EXCEPTION = "exception";

	@PostMapping(path = "/Creation/inscription", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void inscriptionAdmin(@RequestBody InscriptionAdministrateurRequest inscriptionAdministrateurRequest) {
		log.info("Creation du compte Administrateur");
		this.administrateurService.creerAdministrateur(inscriptionAdministrateurRequest);
	}

	@PostMapping(path = "/Modification/ModifCommercant", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void modificationCompteCommercant(@RequestBody InscriptionCommercantRequest inscriptionCommercantRequest) {
		log.info("Modification du compte Commercant");
		String email = inscriptionCommercantRequest.email();
		this.administrateurService.modifierCompteCommercant(email, inscriptionCommercantRequest);
	}

	@PostMapping(path = "/Modification/ModifPerso", consumes = MediaType.APPLICATION_JSON_VALUE)

	public void modificationComptePerso(
			@RequestBody InscriptionAdministrateurRequest inscriptionAdministrateurRequest) {
		log.info("Modification du compte personnel");
		String email = inscriptionAdministrateurRequest.email();
		this.administrateurService.modifierComptePerso(email, inscriptionAdministrateurRequest);
	}

	@PostMapping(path = "/Modification/ModifAdmin", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void modificationCompteAdmin(
			@RequestBody InscriptionAdministrateurRequest inscriptionAdministrateurRequest) {
		log.info("Modification du compte Administrateur");
		String email = inscriptionAdministrateurRequest.email();
		this.administrateurService.modifierCompteAdmin(email, inscriptionAdministrateurRequest);
	}

	@PostMapping(path = "/Modification/ModifSalarie", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void modificationCompteSalarie(@RequestBody InscriptionSalarieRequest inscriptionSalarierRequest) {
		log.info("Modification du compte Salarie");
		String email = inscriptionSalarierRequest.email();
		this.administrateurService.modifierCompteSalarie(email, inscriptionSalarierRequest);
	}

	@PostMapping(path = "/Modification/ModifEmployeur", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void modificationCompteEmployeur(@RequestBody InscriptionEmployeurRequest inscriptionEmployeurRequest) {
		log.info("Modification du compte Employeur");
		String email = inscriptionEmployeurRequest.email();
		this.administrateurService.modifierCompteEmployeur(email, inscriptionEmployeurRequest);
	}

	@GetMapping(path = "/Afficher")
	public ResponseEntity<Object> extraireComptes() {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(administrateurService.extraireComptes());
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(EXCEPTION, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}

	@GetMapping(path = "/Consultation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> consulterComptePerso() {
		log.info("Consultation du compte personnel");
		Map<String, String> infos = this.administrateurService.consulterInfosPersos();
		return ResponseEntity.ok().body(infos);
	}
}
