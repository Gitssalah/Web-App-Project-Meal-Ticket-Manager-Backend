package com.epaynexus.www;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.epaynexus.www.model.CarteVirtuelle;
import com.epaynexus.www.model.Salarie;
import com.epaynexus.www.repository.CarteVirtuelleRepository;
import com.epaynexus.www.repository.TransactionRepository;
import com.epaynexus.www.service.CarteVirtuelleServiceImpl;

import net.minidev.json.JSONObject;

@SpringBootTest
class CarteVirtuelleTest {

    @Mock
    private CarteVirtuelleRepository carteVirtuelleRepository;
    @Mock
    private TransactionRepository transactionRepository;
    
    @InjectMocks
    private CarteVirtuelleServiceImpl carteVirtuelleService;

    private CarteVirtuelle carteVirtuelle;

    @BeforeEach
    void setUp() {
        carteVirtuelle = new CarteVirtuelle(); // Initialisation de la carte virtuelle pour les tests
        carteVirtuelle.setSalarie(new Salarie()); // Simulation d'un salarié associé à la carte
    }

    @Test
    void getSoldeCarteVirtuelleBySalarieIdTest() {
        Long salarieId = 1L; // Identifiant du salarié pour lequel le solde est recherché
        carteVirtuelle.setSolde(500.0); // Solde fictif pour le test
        // Configuration du mock pour simuler le comportement du repository
        when(carteVirtuelleRepository.findBySalarie_Id(salarieId)).thenReturn(Optional.of(carteVirtuelle));
        // Appel de la méthode à tester
        JSONObject jsonObject = carteVirtuelleService.getSoldeCarteVirtuelleBySalarieId(salarieId);
        // Vérification que le solde retourné correspond au solde de la carte virtuelle simulée
        assertEquals(500.0, jsonObject.get("solde"));
    }

    @Test
    void calculerSoldeJournalierTest() {
        Long salarieId = 1L; // Identifiant du salarié pour lequel le solde journalier est calculé
        carteVirtuelle.getSalarie().setForfaitJournalier(40.0); // Forfait journalier fictif pour le test
        // Configuration du mock pour simuler le comportement du repository
        when(carteVirtuelleRepository.findBySalarie_Id(salarieId)).thenReturn(Optional.of(carteVirtuelle));
        // Appel de la méthode à tester
        Double soldeJournalier = carteVirtuelleService.calculerSoldeJournalier(salarieId);
        // Vérification que le solde journalier calculé est correct
        assertEquals(40.0, soldeJournalier);
    }
}
