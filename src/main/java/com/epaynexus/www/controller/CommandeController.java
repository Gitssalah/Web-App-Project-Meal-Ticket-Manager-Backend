package com.epaynexus.www.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.epaynexus.www.dto.CommandeDTO;
import com.epaynexus.www.dto.CreationCommandeRequest;
import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.service.CommandeService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("API/Commande")
public class CommandeController {
	private static final String MESSAGE = "message";
	private static final String EXCEPTION = "exception";
	private final CommandeService commandeService;

	public CommandeController(CommandeService commandeService) {
		this.commandeService = commandeService;
	}

	@PostMapping(value = "/creerCommande")
	public ResponseEntity<JSONObject> creerCommande(@RequestBody CreationCommandeRequest creationCommandeRequest) {
		JSONObject jsonObject = new JSONObject();
		try {
			commandeService.creerCommande(creationCommandeRequest);
			jsonObject.put(MESSAGE, "Commande créée avec succès");
			return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put(EXCEPTION, ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}

	@PutMapping(value = "/executerCommande/{reference}")
	public ResponseEntity<JSONObject> executerCommande(@PathVariable Long reference) {
		JSONObject jsonObject = new JSONObject();
		try {
			commandeService.executerCommande(reference);
			jsonObject.put(MESSAGE, "Commande exécutée et traitée avec succès");
			return ResponseEntity.status(HttpStatus.OK).body(jsonObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put(EXCEPTION, ex.getMessage());
			if (ex instanceof NotFoundException)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}

	@GetMapping(value = "/extraireToutesCommandes")
	public ResponseEntity<List<CommandeDTO>> extraireToutesCommandes() {
		return ResponseEntity.status(HttpStatus.OK).body(commandeService.extraireToutesCommandes());
	}

	@GetMapping(value = "/chercherDetailsCommande/{reference}")
	public ResponseEntity<Object> extraireDetailsDemandeCreationCompte(@PathVariable Long reference) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(commandeService.chercherDetailsCommande(reference));
		} catch (Exception ex) {
			ex.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(EXCEPTION, ex.getMessage());
			if (ex instanceof NotFoundException)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
	}

	@GetMapping(value = "/extraireToutesCommandesByIdEmployeur/{idEmployeur}")
	public ResponseEntity<Object> extraireToutesCommandesByIdEmployeur(@PathVariable Long idEmployeur) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(commandeService.extraireToutesCommandesByIdEmployeur(idEmployeur));
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
