package com.epaynexus.www.mapper;

import org.springframework.stereotype.Component;

import com.epaynexus.www.dto.TransactionDTO;
import com.epaynexus.www.model.Transaction;

import net.minidev.json.JSONObject;

@Component
public class TransactionMapper {

    public TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setIdentifiant(transaction.getIdentifiant());
        dto.setNumCarte(transaction.getNumCarte());
        dto.setMontant(transaction.getMontant());
        dto.setDateCreation(transaction.getDateCreation());
        dto.setCarteVirtuelleDuSalarieId(transaction.getCarteVirtuelle().getSalarie().getId());
        return dto;
    }
    
    public JSONObject convertTransactionToJSON(TransactionDTO transaction) {
        JSONObject transactionJson = new JSONObject();
        transactionJson.put("identifiant", transaction.getIdentifiant());
        transactionJson.put("numCarte", transaction.getNumCarte());
        transactionJson.put("montant", transaction.getMontant());
        transactionJson.put("dateCreation", transaction.getDateCreation());
        transactionJson.put("carteVirtuelleDuSalarieId", transaction.getCarteVirtuelleDuSalarieId());
        return transactionJson;
    }
}