package com.epaynexus.www.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.epaynexus.www.model.Commercant;
import com.epaynexus.www.model.Transaction;



@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> //identifiant est Long 
{
	List<Transaction> findByNumCarteAndDateCreationBetween(String numCarte, Date startOfDay, Date endOfDay);
	List<Transaction> findByCarteVirtuelle_Salarie_IdOrderByDateCreationDesc(Long carteVirtuelleDuSalarieId);
	List<Transaction> findByCarteVirtuelle_Salarie_IdAndDateCreationOrderByDateCreationDesc(Long carteVirtuelleDuSalarieId, Date date);
	List<Transaction> findByCarteVirtuelle_Salarie_IdAndCommercantOrderByDateCreationDesc(Long carteVirtuelleDuSalarieId, Commercant commercant);
}
