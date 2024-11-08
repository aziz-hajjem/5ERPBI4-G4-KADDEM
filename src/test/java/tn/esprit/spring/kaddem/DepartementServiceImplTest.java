package tn.esprit.spring.kaddem;


import tn.esprit.spring.kaddem.entities.Departement;

import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartementServiceImplTest {

    @Mock
    private DepartementRepository departementRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;



    @Test
    void testRetrieveAllDepartements() {
        // Préparation des données de test
        Departement departement1 = new Departement();
        Departement departement2 = new Departement();
        List<Departement> expectedDepartements = Arrays.asList(departement1, departement2);

        // Simulation du comportement du repository
        when(departementRepository.findAll()).thenReturn(expectedDepartements);

        // Appel de la méthode de service
        List<Departement> actualDepartements = departementService.retrieveAllDepartements();

        // Vérification du résultat
        assertEquals(expectedDepartements, actualDepartements);
        verify(departementRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveDepartement() {
        // ID du departement à récupérer
        Integer departementId = 1;
        Departement expectedDepartement = new Departement();

        // Simulation du comportement du repository
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(expectedDepartement));

        // Appel de la méthode de service
        Departement actualDepartement = departementService.retrieveDepartement(departementId);

        // Vérification du résultat
        assertEquals(expectedDepartement, actualDepartement);
        verify(departementRepository, times(1)).findById(departementId);
    }


    @Test
    void testRemoveDepartement() {
        // ID du departement à supprimer
        Integer departementId = 1;
        Departement departement = new Departement();

        // Simulation du comportement du repository
        when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));

        // Appel de la méthode de suppression
        departementService.deleteDepartement(departementId);

        // Vérification que la méthode delete a été appelée
        verify(departementRepository, times(1)).delete(departement);
    }



    @Test
    void testRemoveEtudiantFromDepartement() {
        Integer etudiantId = 101;

        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(etudiantId);
        Departement departement = new Departement();
        etudiant.setDepartement(departement);

        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(etudiant));

        departementService.removeEtudiantFromDepartement(etudiantId);

        // Verify that the student's department was set to null
        assertNull(etudiant.getDepartement());

        // Verify that the student was saved after being disassociated from the department
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testRemoveEtudiantFromDepartementNotFound() {
        Integer etudiantId = 101;

        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.empty());

        departementService.removeEtudiantFromDepartement(etudiantId);

        verify(etudiantRepository, never()).save(any(Etudiant.class));
    }

    @Test
    void testAssignEtudiantsToDepartement() {
        Departement departement = new Departement(1, "Computer Science");
        Etudiant etudiant1 = new Etudiant(1, "John", "Doe", null);
        Etudiant etudiant2 = new Etudiant(2, "Jane", "Doe", null);
        List<Integer> etudiantIds = Arrays.asList(etudiant1.getIdEtudiant(), etudiant2.getIdEtudiant());

        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));
        when(etudiantRepository.findAllById(etudiantIds)).thenReturn(Arrays.asList(etudiant1, etudiant2));

        // Act
        Departement assignedDepartement = departementService.affectDepartementToEtudiants(1, etudiantIds);

        // Assert
        assertEquals(1, assignedDepartement.getIdDepart());
        assertEquals(departement, etudiant1.getDepartement());
        assertEquals(departement, etudiant2.getDepartement());

        verify(etudiantRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testAssignEtudiantsToNonExistingDepartement() {
        List<Integer> etudiantIds = Arrays.asList(1, 2);

        // Mock repository to return empty when searching for a department
        when(departementRepository.findById(1)).thenReturn(Optional.empty());

        Departement result = departementService.affectDepartementToEtudiants(1, etudiantIds);

        // Act & Assert
        assertNull(result);


        verify(etudiantRepository, never()).save(any(Etudiant.class));
    }


    @Test
    void testAssignEtudiantsWhenSomeEtudiantsNotFound() {
        // Arrange
        Departement departement = new Departement(1, "Computer Science");
        Etudiant etudiant1 = new Etudiant(1, "John", "Doe", null);
        List<Integer> etudiantIds = Arrays.asList(etudiant1.getIdEtudiant(), 999); // 999 does not exist

        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));
        when(etudiantRepository.findAllById(etudiantIds)).thenReturn(Collections.singletonList(etudiant1)); // Only one found

        Departement assignedDepartement = departementService.affectDepartementToEtudiants(1, etudiantIds);

        assertEquals(1, assignedDepartement.getIdDepart());
        assertEquals(departement, etudiant1.getDepartement());

        verify(etudiantRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testAssignEtudiantsWithNonExistentIds() {
        // Arrange
        Departement departement = new Departement(1, "Computer Science");
        List<Integer> etudiantIds = Arrays.asList(99, 100); // Non-existent student IDs

        // Mock repository calls
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));
        when(etudiantRepository.findAllById(etudiantIds)).thenReturn(Collections.emptyList());

        // Act
        Departement assignedDepartement = departementService.affectDepartementToEtudiants(1, etudiantIds);

        // Assert
        assertEquals(1, assignedDepartement.getIdDepart());
        verify(etudiantRepository).saveAll(Collections.emptyList());
    }

    @Test
    void testAddDepartement() {
        Departement departement = new Departement();
        when(departementRepository.save(departement)).thenReturn(departement);
        Departement result = departementService.addDepartement(departement);
        assertEquals(departement, result);
        verify(departementRepository, times(1)).save(departement);
    }

    @Test
    void countStudentsInDepartement() {
        Integer departmentId = 1;
        long expectedCount = 5;

        // Mock the repository response
        when(etudiantRepository.countEtudiantsByDepartement_IdDepart(departmentId)).thenReturn(expectedCount);

        // Call the service method
        long actualCount = departementService.countStudentsInDepartement(departmentId);

        // Assert the result
        assertEquals(expectedCount, actualCount);
    }
}