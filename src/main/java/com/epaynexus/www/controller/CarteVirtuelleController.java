package com.epaynexus.www.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epaynexus.www.dto.CarteVirtuelleDTO;
import com.epaynexus.www.exception.CarteVirtuelleNotFoundException;
import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.model.CarteVirtuelle;
import com.epaynexus.www.service.CarteVirtuelleService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("API/CarteVirtuelle")
public class CarteVirtuelleController {
	private static final String MESSAGE = "message";
	private static final String EXCEPTION = "exception";
    private final CarteVirtuelleService carteVirtuelleService;

    public CarteVirtuelleController(CarteVirtuelleService carteVirtuelleService) {
        this.carteVirtuelleService = carteVirtuelleService;
    }

    @PutMapping(value = "/activerCarteVirtuelle/{numero}")
    public ResponseEntity<JSONObject> activerCarteVirtuelle(@PathVariable String numero) {
        JSONObject jsonObject = new JSONObject();
        try {
            carteVirtuelleService.activerCarteVirtuelle(numero);
            jsonObject.put(MESSAGE, "Carte virtuelle activée avec succès");
            return ResponseEntity.status(HttpStatus.OK).body(jsonObject);
        } catch (CarteVirtuelleNotFoundException ex) {
            jsonObject.put(EXCEPTION, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
        }
    }

    @PutMapping(value = "/desactiverCarteVirtuelle/{numero}")
    public ResponseEntity<JSONObject> desactiverCarteVirtuelle(@PathVariable String numero) {
        JSONObject jsonObject = new JSONObject();
        try {
            carteVirtuelleService.desactiverCarteVirtuelle(numero);
            jsonObject.put(MESSAGE, "Carte virtuelle désactivée avec succès");
            return ResponseEntity.status(HttpStatus.OK).body(jsonObject);
        } catch (CarteVirtuelleNotFoundException ex) {
            jsonObject.put(EXCEPTION, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
        }
    }
    
    @GetMapping (value = "/extraireToutesCartes")
    public ResponseEntity<List<CarteVirtuelleDTO>> extraireToutesCartes(){
    	return ResponseEntity.status(HttpStatus.OK).body(carteVirtuelleService.extraireToutesCartes());
    }
    
    @GetMapping(value = "/{salarieId}/solde")
    public ResponseEntity<JSONObject> getSoldeCarteVirtuelleBySalarieId(@PathVariable Long salarieId) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject result= carteVirtuelleService.getSoldeCarteVirtuelleBySalarieId(salarieId);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (NotFoundException ex) {
            jsonObject.put(EXCEPTION, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject.put(EXCEPTION, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
        }
    }

    
    @GetMapping(value = "/{salarieId}/soldeJournalier")
    public ResponseEntity<JSONObject> calculerSoldeJournalier(@PathVariable Long salarieId) {
        JSONObject jsonObject = new JSONObject();
        try {
            Double soldeJournalier = carteVirtuelleService.calculerSoldeJournalier(salarieId);
            CarteVirtuelle carteVirtuelle = carteVirtuelleService.getCarteVirtuelleBySalarieId(salarieId);
            jsonObject.put(MESSAGE, "Solde journalier calculé avec succès");
            jsonObject.put("soldeJournalier", soldeJournalier);
            jsonObject.put("plafondQuotidien", carteVirtuelle.getPlafondQuotidien());
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