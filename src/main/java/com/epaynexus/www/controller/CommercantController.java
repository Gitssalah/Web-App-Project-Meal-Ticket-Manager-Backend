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
import com.epaynexus.www.dto.InscriptionCommercantRequest;
import com.epaynexus.www.service.CommercantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "API/Commercant")
public class CommercantController {

	private CommercantService commercantService;

	@PostMapping(path = "/Modification/ModifPerso", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void modificationCompteCommercant(@RequestBody InscriptionCommercantRequest inscriptionCommercantRequest) {
		log.info("Modification du compte personnel");
		String email = inscriptionCommercantRequest.email();
		this.commercantService.modifierComptePerso(email, inscriptionCommercantRequest);
	}

	@PostMapping(path = "/creer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONObject> creerCommercant(@RequestBody InscriptionCommercantRequest inscriptionCommercantRequest) {
		JSONObject jsonObject = new JSONObject();
		try{
			log.info("Creation du compte Commercant");
			this.commercantService.creerCommercant(inscriptionCommercantRequest);	
			jsonObject.put("message", "Commerçant est crée avec succès");
			return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject);
		}
		catch(Exception e) {
			e.printStackTrace();
			jsonObject.put("exception", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}
	
	@GetMapping(path = "/Consultation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> consulterComptePerso() {
		log.info("Consultation du compte personnel");
		Map<String, String> infos = this.commercantService.consulterInfosPersos();
		return ResponseEntity.ok().body(infos);
	}
}
