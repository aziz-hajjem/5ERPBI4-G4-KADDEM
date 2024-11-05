package tn.esprit.spring.kaddem;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContratTestService {

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ContratServiceImpl contratService;


    @Test
    void testRetrieveAllContrats() {
        Contrat contrat1 = new Contrat();
        Contrat contrat2 = new Contrat();
        List<Contrat> expectedContrats = Arrays.asList(contrat1, contrat2);
        when(contratRepository.findAll()).thenReturn(expectedContrats);
        List<Contrat> actualContrats = contratService.retrieveAllContrats();
        assertEquals(expectedContrats, actualContrats);
        verify(contratRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveContrat() {
        Integer contratId = 1;
        Contrat expectedContrat = new Contrat();
        when(contratRepository.findById(contratId)).thenReturn(Optional.of(expectedContrat));
        Contrat actualContrat = contratService.retrieveContrat(contratId);
        assertEquals(expectedContrat, actualContrat);
        verify(contratRepository, times(1)).findById(contratId);
    }

    @Test
    void testRetrieveContrat_NotFound() {
        Integer contratId = 2;
        when(contratRepository.findById(contratId)).thenReturn(Optional.empty());
        Contrat actualContrat = contratService.retrieveContrat(contratId);
        assertNull(actualContrat);
        verify(contratRepository, times(1)).findById(contratId);
    }

    @Test
    void testRemoveContrat() {
        Integer contratId = 1;
        Contrat contrat = new Contrat();
        when(contratRepository.findById(contratId)).thenReturn(Optional.of(contrat));
        contratService.removeContrat(contratId);
        verify(contratRepository, times(1)).delete(contrat);
    }

    @Test
     void testAffectContratToEtudiant_NoExistingContracts() {
        Integer idContrat = 1;
        String nomE = "Doe";
        String prenomE = "John";
        Etudiant etudiant = new Etudiant();
        etudiant.setContrats(new HashSet<>());
        Contrat contrat = new Contrat();
        when(etudiantRepository.findByNomEAndPrenomE(nomE, prenomE)).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(idContrat)).thenReturn(contrat);
        Contrat result = contratService.affectContratToEtudiant(idContrat, nomE, prenomE);
        assertEquals(etudiant, result.getEtudiant());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
     void testAffectContratToEtudiant_ActiveContractsUnderLimit() {
        Integer idContrat = 1;
        String nomE = "Doe";
        String prenomE = "John";
        Contrat activeContract1 = new Contrat();
        activeContract1.setArchive(true);
        Contrat activeContract2 = new Contrat();
        activeContract2.setArchive(true);
        Set<Contrat> existingContracts = new HashSet<>();
        existingContracts.add(activeContract1);
        existingContracts.add(activeContract2);
        Etudiant etudiant = new Etudiant();
        etudiant.setContrats(existingContracts);
        Contrat contrat = new Contrat();
        when(etudiantRepository.findByNomEAndPrenomE(nomE, prenomE)).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(idContrat)).thenReturn(contrat);
        Contrat result = contratService.affectContratToEtudiant(idContrat, nomE, prenomE);
        assertEquals(etudiant, result.getEtudiant());
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
     void testAffectContratToEtudiant_ActiveContractsExceedsLimit() {
        Integer idContrat = 1;
        String nomE = "Doe";
        String prenomE = "John";
        Contrat activeContract1 = new Contrat();
        activeContract1.setArchive(true);
        Contrat activeContract2 = new Contrat();
        activeContract2.setArchive(true);
        Contrat activeContract3 = new Contrat();
        activeContract3.setArchive(true);
        Contrat activeContract4 = new Contrat();
        activeContract4.setArchive(true);
        Contrat activeContract5 = new Contrat();
        activeContract5.setArchive(true);
        Set<Contrat> existingContracts = new HashSet<>();
        existingContracts.add(activeContract1);
        existingContracts.add(activeContract2);
        existingContracts.add(activeContract3);
        existingContracts.add(activeContract4);
        existingContracts.add(activeContract5);
        Etudiant etudiant = new Etudiant();
        etudiant.setContrats(existingContracts);
        Contrat contrat = new Contrat();
        when(etudiantRepository.findByNomEAndPrenomE(nomE, prenomE)).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(idContrat)).thenReturn(contrat);
        Contrat result = contratService.affectContratToEtudiant(idContrat, nomE, prenomE);
        assertNull(result.getEtudiant()); // Contract should not be assigned due to exceeded limit
        verify(contratRepository, never()).save(contrat);
    }

    @Test
     void testAffectContratToEtudiant_NullArchiveField() {
        Integer idContrat = 1;
        String nomE = "Doe";
        String prenomE = "John";
        Contrat contractWithNullArchive = new Contrat();
        contractWithNullArchive.setArchive(null);
        Set<Contrat> existingContracts = new HashSet<>();
        existingContracts.add(contractWithNullArchive);
        Etudiant etudiant = new Etudiant();
        etudiant.setContrats(existingContracts);
        Contrat contrat = new Contrat();
        when(etudiantRepository.findByNomEAndPrenomE(nomE, prenomE)).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(idContrat)).thenReturn(contrat);
        Contrat result = contratService.affectContratToEtudiant(idContrat, nomE, prenomE);
        assertEquals(etudiant, result.getEtudiant());
        verify(contratRepository, times(1)).save(contrat);
    }
    @Test
    void testRetrieveAndUpdateStatusContrat() {
        Contrat contrat1 = new Contrat();
        contrat1.setDateFinContrat(new Date(System.currentTimeMillis() - 15L * 24 * 60 * 60 * 1000)); // 15 days ago
        contrat1.setArchive(false);
        Contrat contrat2 = new Contrat();
        contrat2.setDateFinContrat(new Date()); // today
        contrat2.setArchive(false);
        List<Contrat> allContrats = Arrays.asList(contrat1, contrat2);
        when(contratRepository.findAll()).thenReturn(allContrats);
        contratService.retrieveAndUpdateStatusContrat();
        verify(contratRepository, times(1)).save(contrat2); // Only contrat2 should be saved as archived
        assertFalse(contrat1.getArchive());
        assertTrue(contrat2.getArchive());
    }

    @Test
     void testUpdateContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.save(contrat)).thenReturn(contrat);
        Contrat result = contratService.updateContrat(contrat);
        assertEquals(contrat, result);
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
     void testAddContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.save(contrat)).thenReturn(contrat);
        Contrat result = contratService.addContrat(contrat);
        assertEquals(contrat, result);
        verify(contratRepository, times(1)).save(contrat);
    }

    @Test
     void testNbContratsValides() {
        Date startDate = new Date();
        Date endDate = new Date();
        Integer expectedCount = 5;
        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(expectedCount);
        Integer result = contratService.nbContratsValides(startDate, endDate);
        assertEquals(expectedCount, result);
        verify(contratRepository, times(1)).getnbContratsValides(startDate, endDate);
    }
@Test
     void testGetChiffreAffaireEntreDeuxDates() {
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + (30L * 24 * 60 * 60 * 1000));
        Contrat contrat1 = new Contrat();
        contrat1.setSpecialite(Specialite.IA);
        Contrat contrat2 = new Contrat();
        contrat2.setSpecialite(Specialite.CLOUD);
        Contrat contrat3 = new Contrat();
        contrat3.setSpecialite(Specialite.RESEAUX);
        List<Contrat> contrats = Arrays.asList(contrat1, contrat2, contrat3);
        when(contratRepository.findAll()).thenReturn(contrats);
        float expectedChiffreAffaire = (30f / 30) * (300 + 400 + 350);
        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);
        assertEquals(expectedChiffreAffaire, result, 0.01);
        verify(contratRepository, times(1)).findAll();
    }
    @Test
    void testGetChiffreAffaireEntreDeuxDates_NoContracts() {
        // Arrange
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + (30L * 24 * 60 * 60 * 1000)); // 1 month later

        // Mock an empty list of contracts
        when(contratRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert
        assertEquals(0, result, "Expected chiffre d'affaires to be 0 when no contracts are found.");
        verify(contratRepository, times(1)).findAll();
    }




}