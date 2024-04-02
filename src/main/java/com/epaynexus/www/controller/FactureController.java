package com.epaynexus.www.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.service.FactureService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("API/Facture")
public class FactureController {
	private static final String MESSAGE = "message";
	private static final String EXCEPTION = "exception";
	private final FactureService factureService;
	
	public FactureController(FactureService factureService) {
		this.factureService = factureService;
	}
	
	@GetMapping("/getFacture/{reference}")
	public ResponseEntity<JSONObject> getFacture(@PathVariable Long reference) {
	    JSONObject jsonObject = new JSONObject();
	    try {
	        Object facture = factureService.getFacture(reference);
	        jsonObject.put(MESSAGE, "Facture récupérée avec succès");
	        jsonObject.put("facture", facture);
	        return ResponseEntity.status(HttpStatus.OK).body(jsonObject);
	    } catch (NotFoundException ex) {
	        jsonObject.put(EXCEPTION, ex.getMessage());
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        jsonObject.put(EXCEPTION, ex.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
	    }
	}
}
