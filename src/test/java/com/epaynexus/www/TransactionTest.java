package com.epaynexus.www;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.epaynexus.www.dto.TransactionDTO;
import com.epaynexus.www.dto.TransactionRequest;
import com.epaynexus.www.mapper.TransactionMapper;
import com.epaynexus.www.model.CarteVirtuelle;
import com.epaynexus.www.model.Commercant;
import com.epaynexus.www.model.Salarie;
import com.epaynexus.www.model.Transaction;
import com.epaynexus.www.repository.TransactionRepository;
import com.epaynexus.www.service.TransactionServiceImpl;



@SpringBootTest
class TransactionTest {
	private static TransactionRequest transactionRequest;
	
	@Mock
	private TransactionRepository transactionRepository;
	@Mock
	private TransactionMapper transactionMapper;
	
	@InjectMocks
	private TransactionServiceImpl transactionService;

	@BeforeEach
	void init() {
		transactionRequest = new TransactionRequest("123456789",100.00,2L);
		// Configuration du mock pour simuler le comportement de la méthode convertToDTO de TransactionMapper
	    when(transactionMapper.convertToDTO(any(Transaction.class))).thenAnswer(invocation -> {
	        Transaction transaction = invocation.getArgument(0);
	        TransactionDTO dto = new TransactionDTO();
	        dto.setIdentifiant(transaction.getIdentifiant());
	        dto.setNumCarte(transaction.getNumCarte());
	        dto.setMontant(transaction.getMontant());
	        dto.setDateCreation(transaction.getDateCreation());
	        dto.setCarteVirtuelleDuSalarieId(transaction.getCarteVirtuelle().getSalarie().getId());
	        dto.setCommercantId(transaction.getCommercant().getId());
	        return dto;
	    });
	}

	@Test
	void creerDemandeTest() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
		Transaction demande = transactionService.ajouterTransaction(transactionRequest);
		assertNotNull(demande, "demande instance should not be null");
	}
	
	@Test
	void getTransactionsTest() {
	    // Création de données de test
	    Long salarieId = 1L;
	    Commercant commercant = new Commercant();
	    commercant.setId(1L);
	    List<Transaction> mockTransactions = new ArrayList<>();
	    
	    // Création d'une carte virtuelle avec un salarié associé
	    CarteVirtuelle carteVirtuelle = new CarteVirtuelle();
	    Salarie salarie = new Salarie();
	    salarie.setId(salarieId); // Assurez-vous que l'ID du salarié est défini
	    carteVirtuelle.setSalarie(salarie); // Associez le salarié à la carte virtuelle
	    
	    // Création de transactions associées à la carte virtuelle
	    mockTransactions.add(new Transaction(1L, "1234", 100.0, new Date(), carteVirtuelle,commercant));
	    mockTransactions.add(new Transaction(2L, "1234", 200.0, new Date(), carteVirtuelle,commercant));

	    // Configuration du mock pour simuler le comportement de la méthode findByCarteVirtuelle_Salarie_IdOrderByDateCreationDesc
	    when(transactionRepository.findByCarteVirtuelle_Salarie_IdOrderByDateCreationDesc(salarieId)).thenReturn(mockTransactions);

	    // Appel de la méthode à tester
	    List<TransactionDTO> result = transactionService.getTransactions(salarieId);

	    // Vérification du résultat
	    assertEquals(2, result.size());
	    assertEquals(1L, result.get(0).getIdentifiant());
	    assertEquals("1234", result.get(0).getNumCarte());
	    assertEquals(100.0, result.get(0).getMontant());
	    
	    // Test avec un ID de salarié sans transactions
	    List<TransactionDTO> emptyResult = transactionService.getTransactions(2L);
	    assertTrue(emptyResult.isEmpty(), "La liste de transactions devrait être vide pour un ID de salarié sans transactions");
	}
	
	@Test
	void getTransactionsByDateTest() {
	    // Création de données de test
	    Long salarieId = 1L;
	    Commercant commercant = new Commercant();
	    commercant.setId(1L);
	    Date date = new Date();
	    List<Transaction> mockTransactions = new ArrayList<>();
	    
	    // Création d'une carte virtuelle avec un salarié associé
	    CarteVirtuelle carteVirtuelle = new CarteVirtuelle();
	    Salarie salarie = new Salarie();
	    salarie.setId(salarieId); // Assurez-vous que l'ID du salarié est défini
	    carteVirtuelle.setSalarie(salarie); // Associez le salarié à la carte virtuelle
	    
	    // Création de transactions associées à la carte virtuelle
	    mockTransactions.add(new Transaction(1L, "1234", 100.0, date, carteVirtuelle, commercant));
	    mockTransactions.add(new Transaction(2L, "1234", 200.0, date, carteVirtuelle, commercant));

	    // Configuration du mock pour simuler le comportement de la méthode findByCarteVirtuelle_Salarie_IdOrderByDateCreationDesc
	    when(transactionRepository.findByCarteVirtuelle_Salarie_IdAndDateCreationOrderByDateCreationDesc(salarieId,date)).thenReturn(mockTransactions);

	    // Appel de la méthode à tester
	    List<TransactionDTO> result = transactionService.getTransactionsByDate(salarieId, date);

	    // Vérification du résultat
	    assertEquals(2, result.size());
	    assertEquals(1L, result.get(0).getIdentifiant());
	    assertEquals("1234", result.get(0).getNumCarte());
	    assertEquals(100.0, result.get(0).getMontant());
	    
	    // Test avec un ID de salarié sans transactions
	    List<TransactionDTO> emptyResult = transactionService.getTransactions(2L);
	    assertTrue(emptyResult.isEmpty(), "La liste de transactions devrait être vide pour un ID de salarié sans transactions");
	}
	
	@Test
	void getTransactionsByCommercantTest() {
	    // Création de données de test
	    Long salarieId = 1L;
	    Commercant commercant = new Commercant();
	    commercant.setId(1L);
	    Date date = new Date();
	    List<Transaction> mockTransactions = new ArrayList<>();
	    
	    // Création d'une carte virtuelle avec un salarié associé
	    CarteVirtuelle carteVirtuelle = new CarteVirtuelle();
	    Salarie salarie = new Salarie();
	    salarie.setId(salarieId); // Assurez-vous que l'ID du salarié est défini
	    carteVirtuelle.setSalarie(salarie); // Associez le salarié à la carte virtuelle
	    
	    // Création de transactions associées à la carte virtuelle
	    mockTransactions.add(new Transaction(1L, "1234", 100.0, date, carteVirtuelle, commercant));
	    mockTransactions.add(new Transaction(2L, "1234", 200.0, date, carteVirtuelle, commercant));

	    // Configuration du mock pour simuler le comportement de la méthode findByCarteVirtuelle_Salarie_IdOrderByDateCreationDesc
	    when(transactionRepository.findByCarteVirtuelle_Salarie_IdAndCommercantOrderByDateCreationDesc(salarieId,commercant)).thenReturn(mockTransactions);

	    // Appel de la méthode à tester
	    List<TransactionDTO> result = transactionService.getTransactionsByCommercant(salarieId, commercant);

	    // Vérification du résultat
	    assertEquals(2, result.size());
	    assertEquals(1L, result.get(0).getIdentifiant());
	    assertEquals("1234", result.get(0).getNumCarte());
	    assertEquals(100.0, result.get(0).getMontant());
	    
	    // Test avec un ID de salarié sans transactions
	    List<TransactionDTO> emptyResult = transactionService.getTransactions(2L);
	    assertTrue(emptyResult.isEmpty(), "La liste de transactions devrait être vide pour un ID de salarié sans transactions");
	}
}