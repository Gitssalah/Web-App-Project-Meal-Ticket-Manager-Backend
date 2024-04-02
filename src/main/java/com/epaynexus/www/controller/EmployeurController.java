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
import com.epaynexus.www.dto.InscriptionEmployeurRequest;
import com.epaynexus.www.dto.InscriptionSalarieRequest;
import com.epaynexus.www.service.EmployeurService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "API/Employeur")
public class EmployeurController {

	private EmployeurService employeurService;
	private static final String EXCEPTION = "exception";

	@PostMapping(path = "/Modification/ModifPerso", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void modificationComptePerso(@RequestBody InscriptionEmployeurRequest inscriptionEmployeurRequest) {
		log.info("Modification du compte personnel");
		String email = inscriptionEmployeurRequest.email();
		this.employeurService.modifierComptePerso(email, inscriptionEmployeurRequest);
	}

	@PostMapping(path = "/Modification/ModifSalarie", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void modificationCompteSalarie(@RequestBody InscriptionSalarieRequest inscriptionSalarieRequest) {
		log.info("Modification du compte Salarie");
		String email = inscriptionSalarieRequest.email();
		this.employeurService.modifierCompteSalarie(email, inscriptionSalarieRequest);
	}

	@PostMapping(path = "/creer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<JSONObject> creerEmployeur(@RequestBody InscriptionEmployeurRequest inscriptionEmployeurRequest) {
		JSONObject jsonObject = new JSONObject();
		try{
			log.info("Creation du compte Employeur");
			this.employeurService.creerEmployeur(inscriptionEmployeurRequest);
			jsonObject.put("message", "Employeur est crée avec succès");
			return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject);
		}
		catch(Exception e) {
			e.printStackTrace();
			jsonObject.put(EXCEPTION, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}
	
    @GetMapping(path = "/AfficherSalaries")
    public ResponseEntity<Object> extraireComptes() {
    	try {
			return ResponseEntity.status(HttpStatus.OK).body(employeurService.extraireComptesSalaries());
		}
		catch(Exception e) {
			e.printStackTrace();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put(EXCEPTION, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
    }
    
	@GetMapping(path = "/Consultation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, String>> consulterComptePerso() {
		log.info("Consultation du compte personnel");
		Map<String, String> infos = this.employeurService.consulterInfosPersos();
		return ResponseEntity.ok().body(infos);
	}
}
