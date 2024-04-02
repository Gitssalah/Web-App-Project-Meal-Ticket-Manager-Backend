package com.epaynexus.www.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epaynexus.www.dto.TransactionDTO;
import com.epaynexus.www.dto.TransactionRequest;
import com.epaynexus.www.mapper.TransactionMapper;
import com.epaynexus.www.model.CarteVirtuelle;
import com.epaynexus.www.model.Commercant;
import com.epaynexus.www.model.Transaction;
import com.epaynexus.www.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;
	private final CarteVirtuelleService carteVirtuelleService;
	private final CommercantService commercantService;
	private final TransactionMapper transactionMapper;
	
	@Autowired
	public TransactionServiceImpl(TransactionRepository transactionRepository, CarteVirtuelleService carteVirtuelleService,CommercantService commercantService, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.carteVirtuelleService = carteVirtuelleService;
        this.commercantService = commercantService;
        this.transactionMapper = transactionMapper;
    }
	
	@Override
	public Transaction ajouterTransaction(TransactionRequest transactionRequest) {
		CarteVirtuelle carteVirtuelle = carteVirtuelleService.getCarteVirtuelleByNum(transactionRequest.numCarte());
		Commercant commercant = commercantService.getCommercantById(transactionRequest.commercantId());
		Transaction transaction = Transaction.builder()
				.numCarte(transactionRequest.numCarte())
				.montant(transactionRequest.montant())
				.dateCreation(new Date())
				.carteVirtuelle(carteVirtuelle)
				.commercant(commercant)
				.build();
		transaction = transactionRepository.save(transaction);
		carteVirtuelleService.modifierSolde(transactionRequest.numCarte(), transactionRequest.montant());
		return transaction;
		
	}
	@Override
	public Boolean verifierValiditerCarte(String numCarte, Double montant) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date startOfDay = calendar.getTime();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		Date endOfDay = calendar.getTime();
		Double depenseDuJour = 0.0;
		List<Transaction> listTransactionDuNumDuJour = transactionRepository.findByNumCarteAndDateCreationBetween(numCarte, startOfDay, endOfDay);
		for(int i = 0; i< listTransactionDuNumDuJour.size();i++) {
			depenseDuJour += listTransactionDuNumDuJour.get(i).getMontant();
		}
		return(carteVirtuelleService.verifierCarte(numCarte,montant,depenseDuJour));
	}

	@Override
	public List<TransactionDTO> getTransactions(Long carteVirtuelleDuSalarieId) {
		List<Transaction> transactions = transactionRepository.findByCarteVirtuelle_Salarie_IdOrderByDateCreationDesc(carteVirtuelleDuSalarieId);
        return transactions.stream()
                .map(transactionMapper::convertToDTO)
                .toList();
	}

	@Override
	public List<TransactionDTO> getTransactionsByDate(Long carteVirtuelleDuSalarieId, Date date) {
	    List<Transaction> transactions = transactionRepository.findByCarteVirtuelle_Salarie_IdAndDateCreationOrderByDateCreationDesc(carteVirtuelleDuSalarieId, date);
	    return transactions.stream()
	            .map(transactionMapper::convertToDTO)
	            .toList();
	}

	@Override
	public List<TransactionDTO> getTransactionsByCommercant(Long carteVirtuelleDuSalarieId, Commercant commercant) {
		List<Transaction> transactions = transactionRepository.findByCarteVirtuelle_Salarie_IdAndCommercantOrderByDateCreationDesc(carteVirtuelleDuSalarieId, commercant);
	    return transactions.stream()
	            .map(transactionMapper::convertToDTO)
	            .toList();
	}
}
