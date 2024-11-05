package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContratServiceImplTest {

    @InjectMocks
    private ContratServiceImpl contratService;

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllContrats() {
        List<Contrat> contrats = new ArrayList<>();
        contrats.add(new Contrat());
        when(contratRepository.findAll()).thenReturn(contrats);

        List<Contrat> result = contratService.retrieveAllContrats();

        assertEquals(1, result.size());
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void testAddContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.addContrat(contrat);

        assertNotNull(result);
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void testUpdateContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.updateContrat(contrat);

        assertNotNull(result);
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void testRetrieveContrat() {
        Integer idContrat = 1;
        Contrat contrat = new Contrat();
        when(contratRepository.findById(idContrat)).thenReturn(Optional.of(contrat));

        Contrat result = contratService.retrieveContrat(idContrat);

        assertNotNull(result);
        verify(contratRepository, times(1)).findById(idContrat);
    }

    @Test
    void testRemoveContrat() {
        Integer idContrat = 1;
        Contrat contrat = new Contrat();
        when(contratRepository.findById(idContrat)).thenReturn(Optional.of(contrat));

        contratService.removeContrat(idContrat);

        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
    void testAffectContratToEtudiant() {
        Integer idContrat = 1;
        String nomE = "John";
        String prenomE = "Doe";
        Etudiant etudiant = new Etudiant();
        etudiant.setContrats(new HashSet<>());

        Contrat contrat = new Contrat();
        contrat.setArchive(false);

        when(etudiantRepository.findByNomEAndPrenomE(nomE, prenomE)).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(idContrat)).thenReturn(contrat);
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.affectContratToEtudiant(idContrat, nomE, prenomE);

        assertNotNull(result);
        assertEquals(etudiant, result.getEtudiant());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
    void testGetChiffreAffaireEntreDeuxDates() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 60L * 24 * 60 * 60 * 1000); // 60 days later
        Contrat contrat1 = new Contrat();
        contrat1.setSpecialite(Specialite.IA);
        
        Contrat contrat2 = new Contrat();
        contrat2.setSpecialite(Specialite.CLOUD);

        List<Contrat> contrats = List.of(contrat1, contrat2);
        when(contratRepository.findAll()).thenReturn(contrats);

        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        assertEquals(42000.0f, result); // (60 days -> 2 months * 300 + 2 months * 400)
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void testNbContratsValides() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 10 * 24 * 60 * 60 * 1000); // 10 days later
        Integer expectedValidContracts = 5;

        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(expectedValidContracts);

        Integer result = contratService.nbContratsValides(startDate, endDate);

        assertEquals(expectedValidContracts, result);
        verify(contratRepository, times(1)).getnbContratsValides(startDate, endDate);
    }

    @Test
    void testRetrieveAndUpdateStatusContrat() {
        Date endDate = new Date();
        Contrat contrat = new Contrat();
        contrat.setArchive(false);
        contrat.setDateFinContrat(endDate);

        List<Contrat> contrats = new ArrayList<>();
        contrats.add(contrat);

        when(contratRepository.findAll()).thenReturn(contrats);
        when(contratRepository.save(any(Contrat.class))).thenReturn(contrat);

        contratService.retrieveAndUpdateStatusContrat();

        verify(contratRepository, times(1)).save(contrat);
    }
}
