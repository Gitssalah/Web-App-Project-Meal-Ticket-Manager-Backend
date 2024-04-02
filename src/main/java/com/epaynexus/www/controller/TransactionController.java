package com.epaynexus.www.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epaynexus.www.dto.TransactionDTO;
import com.epaynexus.www.dto.TransactionRequest;
import com.epaynexus.www.exception.NotFoundException;
import com.epaynexus.www.mapper.TransactionMapper;
import com.epaynexus.www.model.Commercant;
import com.epaynexus.www.service.TransactionService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("API/Transaction")
public class TransactionController {
	private static final String MESSAGE = "message";
	private static final String EXCEPTION = "exception";
	private final TransactionService transactionService;
    private TransactionMapper transactionMapper;
	
	@Autowired
	public TransactionController (TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@PostMapping(value = "/ajouterTransaction")
	public ResponseEntity<JSONObject> ajouterTransaction(@RequestBody TransactionRequest tranRequest) {
		JSONObject jsonObject = new JSONObject();
		try {
			transactionService.ajouterTransaction(tranRequest);
			jsonObject.put(MESSAGE, "Demande créée avec succès");
			return ResponseEntity.status(HttpStatus.CREATED).body(jsonObject);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put(EXCEPTION, ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
		}
}

	@GetMapping(value = "/verifierValiditerCarte/{numCarte}/{montant}")
	public ResponseEntity<JSONObject> verifierValiditerCarte(@PathVariable String numCarte, @PathVariable Double montant) {
		JSONObject jsonObject = new JSONObject();
		try {
			 boolean isValid = transactionService.verifierValiditerCarte(numCarte, montant);
		     jsonObject.put("isValid", isValid);
		     return ResponseEntity.status(HttpStatus.OK).body(jsonObject);
		}
		catch(NotFoundException ex) {
			jsonObject.put(EXCEPTION, ex.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonObject);
		} catch (Exception ex) {
            ex.printStackTrace();
            jsonObject.put(EXCEPTION, ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonObject);
        }
	} 

	@GetMapping(value = "/getTransactions/{carteVirtuelleDuSalarieId}")
    public ResponseEntity<JSONObject> getTransactions(@PathVariable Long carteVirtuelleDuSalarieId) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<TransactionDTO> transactions = transactionService.getTransactions(carteVirtuelleDuSalarieId);
            // Créer une instance de TransactionMapper
            transactionMapper = new TransactionMapper();
            // Mapper chaque TransactionDTO en JSON et collecter dans une liste
            List<JSONObject> jsonTransactions = transactions.stream()
                    .map(transactionMapper::convertTransactionToJSON)
                    .toList();
            // Créer un StringBuilder pour construire le tableau JSON
            StringBuilder transactionsArray = new StringBuilder("[");
            // Ajouter chaque JSONObject au tableau JSON
            for (JSONObject jsonTransaction : jsonTransactions) {
                transactionsArray.append(jsonTransaction.toString()).append(",");
            }
            // Supprimer la virgule finale
            transactionsArray.deleteCharAt(transactionsArray.length() - 1);
            // Fermer le tableau JSON
            transactionsArray.append("]");
            // Ajouter le tableau de transactions à l'objet JSONObject
            jsonObject.put(MESSAGE, "Transactions récupérées avec succès");
            if(!transactions.isEmpty()) {
                jsonObject.put("transactions", transactionsArray);
            }
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
	
	@GetMapping(value = "/getTransactionsByDate/{carteVirtuelleDuSalarieId}/{date}")
    public ResponseEntity<JSONObject> getTransactionsByDate(@PathVariable Long carteVirtuelleDuSalarieId, @PathVariable Date date) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByDate(carteVirtuelleDuSalarieId, date);
            // Convert transactions to JSON
            List<JSONObject> jsonTransactions = transactions.stream()
                    .map(transactionMapper::convertTransactionToJSON)
                    .toList();
            // Build the JSON response
            jsonObject.put(MESSAGE, "Transactions récupérées avec succès pour la date " + date);
            jsonObject.put("transactions", jsonTransactions);
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
	
	@GetMapping(value = "/getTransactionsByDate/{carteVirtuelleDuSalarieId}/{commercant}")
    public ResponseEntity<JSONObject> getTransactionsByDate(@PathVariable Long carteVirtuelleDuSalarieId, @PathVariable Commercant commercant) {
        JSONObject jsonObject = new JSONObject();
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByCommercant(carteVirtuelleDuSalarieId, commercant);
            // Convert transactions to JSON
            List<JSONObject> jsonTransactions = transactions.stream()
                    .map(transactionMapper::convertTransactionToJSON)
                    .toList();
            // Build the JSON response
            jsonObject.put(MESSAGE, "Transactions récupérées avec succès pour le commerçant " + commercant);
            jsonObject.put("transactions", jsonTransactions);
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