package com.epaynexus.www;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.epaynexus.www.model.Commande;
import com.epaynexus.www.model.Facture;
import com.epaynexus.www.repository.FactureRepository;
import com.epaynexus.www.service.FactureServiceImpl;

@SpringBootTest
class FactureTest {

    @Mock
    private FactureRepository factureRepository;

    @InjectMocks
    private FactureServiceImpl factureService;

    private Commande commande;

    @BeforeEach
    void setUp() {
        commande = new Commande(); // Initialisation de la commande pour les tests
    }

    @Test
    void genererFactureTest() {
        // Configuration du mock pour simuler le comportement du repository
        when(factureRepository.save(any(Facture.class))).thenReturn(new Facture());
        // Appel de la méthode à tester
        Facture facture = factureService.genererFacture(commande);
        // Vérification que la facture retournée n'est pas nulle
        assertNotNull(facture);
    }

    @Test
    void getFactureTest() {
        Long reference = 1L; // Référence de la commande pour laquelle une facture est recherchée
        // Configuration du mock pour simuler le comportement du repository
        when(factureRepository.findByCommande_Reference(reference)).thenReturn(Optional.of(new Facture()));
        // Appel de la méthode à tester
        Facture facture = factureService.getFacture(reference);
        // Vérification que la facture retournée n'est pas nulle
        assertNotNull(facture);
    }
}
