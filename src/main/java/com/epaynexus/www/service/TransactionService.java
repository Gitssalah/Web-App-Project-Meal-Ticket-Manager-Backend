package com.epaynexus.www.service;



import java.util.Date;
import java.util.List;

import com.epaynexus.www.dto.TransactionDTO;
import com.epaynexus.www.dto.TransactionRequest;
import com.epaynexus.www.model.Commercant;
import com.epaynexus.www.model.Transaction;

public interface TransactionService {
	Transaction ajouterTransaction(TransactionRequest transactionRequest);
	Boolean verifierValiditerCarte(String numCarte,Double montant);
	List<TransactionDTO> getTransactions(Long carteVirtuelleDuSalarieId);
	List<TransactionDTO> getTransactionsByDate(Long carteVirtuelleDuSalarieId, Date date);
	List<TransactionDTO> getTransactionsByCommercant(Long carteVirtuelleDuSalarieId, Commercant commercant);
}
