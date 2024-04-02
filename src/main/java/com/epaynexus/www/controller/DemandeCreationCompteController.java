package com.epaynexus.www.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.epaynexus.www.dto.DemandeCreationCompteRequest;
import com.epaynexus.www.enumeration.DemandeCreationCompteActionEnum;
import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.service.DemandeCreationCompteService;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("API/DemandeCreationCompte")
public class DemandeCreationCompteController {
	private static final String MESSAGE = "message";
	private static final String EXCEPTION = "exception";
	private final DemandeCreationCompteService demandeCreationCompteService;

	public DemandeCreationCompteController(DemandeCreationCompteService demandeCCService) {
		demandeCreationCompteService = demandeCCService;
	}

	@PostMapping(value = "/creerDemande")
	public ResponseEntity<JSONObject> creerDemande(
			@RequestBody DemandeCreationCompteRequest demaCreationCompteRequest) {
		JSONObject jsonObject = new JSONObject();
		try {
			demandeCreationCompteService.creerDemande(demaCreationCompteRequest);
			jsonObject.put(MESSAGE, "Demande créée avec succès");
			return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put(EXCEPTION, ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}

	@PutMapping(value = "/validerDemande/{identifiantAdmin}/{identifiantDemande}")
	public ResponseEntity<JSONObject> validerDemande(@PathVariable("identifiantAdmin") Long identifiantAdmin,
			@PathVariable("identifiantDemande") Long identifiantDemande) {
		JSONObject jsonObject = new JSONObject();
		try {
			demandeCreationCompteService.traiterDemande(identifiantAdmin, identifiantDemande,
					DemandeCreationCompteActionEnum.VALIDER);
			jsonObject.put(MESSAGE, "Demande validée avec succès");
			return ResponseEntity.status(HttpStatus.OK).body(jsonObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put(EXCEPTION, ex.getMessage());
			if (ex instanceof NotFoundException)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}

	@PutMapping(value = "/rejeterDemande/{identifiantAdmin}/{identifiantDemande}")
	public ResponseEntity<JSONObject> rejeterDemande(@PathVariable("identifiantAdmin") Long identifiantAdmin,
			@PathVariable("identifiantDemande") Long identifiantDemande) {
		JSONObject jsonObject = new JSONObject();
		try {
			demandeCreationCompteService.traiterDemande(identifiantAdmin, identifiantDemande,
					DemandeCreationCompteActionEnum.REJETER);
			jsonObject.put(MESSAGE, "Demande rejetée avec succès");
			return ResponseEntity.status(HttpStatus.OK).body(jsonObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put(EXCEPTION, ex.getMessage());
			if (ex instanceof NotFoundException)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}

	@GetMapping(value = "/extraireToutesDemandes")
	public ResponseEntity<Object> extraireToutesDemandesCreationCompte() {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(demandeCreationCompteService.extraireDemandesCreationCompte());
		}
		catch(Exception e) {
			e.printStackTrace();
			JSONObject jsonObject=new JSONObject();
			jsonObject.put(EXCEPTION, e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}

	@GetMapping(value = "/extraireDetailsDemande/{identifiant}")
	public ResponseEntity<Object> extraireDetailsDemandeCreationCompte(@PathVariable Long identifiant) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(demandeCreationCompteService.chercherDemandeCreationCompte(identifiant));
		} catch (Exception ex) {
			ex.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(EXCEPTION, ex.getMessage());
			if (ex instanceof NotFoundException)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}

	}
}
