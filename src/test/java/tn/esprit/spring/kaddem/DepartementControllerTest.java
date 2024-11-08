package tn.esprit.spring.kaddem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.controllers.DepartementRestController;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.DepartementDTO;
import tn.esprit.spring.kaddem.services.IDepartementService;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class DepartementControllerTest {

    @Mock
    private IDepartementService departementService;

    @InjectMocks
    private DepartementRestController departementRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDepartements() {
        Departement departement1 = new Departement();
        departement1.setIdDepart(1);
        Departement departement2 = new Departement();
        departement2.setIdDepart(2);
        List<Departement> expectedDepartements = Arrays.asList(departement1, departement2);
        when(departementService.retrieveAllDepartements()).thenReturn(expectedDepartements);
        List<Departement> actualDepartements = departementRestController.getDepartements();
        assertEquals(expectedDepartements, actualDepartements);
        verify(departementService, times(1)).retrieveAllDepartements();
    }

    @Test
    void testRetrieveDepartement() {
        Integer departementId = 1;
        Departement expectedDepartement = new Departement();
        expectedDepartement.setIdDepart(departementId);
        when(departementService.retrieveDepartement(departementId)).thenReturn(expectedDepartement);
        Departement actualDepartement = departementRestController.retrieveDepartement(departementId);
        assertEquals(expectedDepartement, actualDepartement);
        verify(departementService, times(1)).retrieveDepartement(departementId);
    }

    @Test
    void testRemoveDepartement() {
        Integer departementId = 1;
        doNothing().when(departementService).deleteDepartement(departementId);
        departementRestController.removeDepartement(departementId);
        verify(departementService, times(1)).deleteDepartement(departementId);
    }

    @Test
    void testAddDepartement() {
        // Création de l'objet DepartementDTO pour le test
        DepartementDTO departementDTO = new DepartementDTO();
        departementDTO.setNomDep("Informatique");


        Departement expectedDepartement = new Departement();
        expectedDepartement.setNomDepart("Informatique");


        when(departementService.addDepartement(any(Departement.class))).thenReturn(expectedDepartement);

        Departement result = departementRestController.addDepartement(departementDTO);
        assertEquals(expectedDepartement, result);
    }

    @Test
    void testAssignDepartementToEtudiant() {
        Integer idDepartement = 1;
        List<Integer> etudiantIds = Arrays.asList(1, 2);

        Departement expectedDepartement = new Departement();
        expectedDepartement.setIdDepart(idDepartement);
        when(departementService.affectDepartementToEtudiants(idDepartement, etudiantIds)).thenReturn(expectedDepartement);
        Departement actualDepartement = departementRestController.assignDepartementToEtudiants(idDepartement, etudiantIds);
        assertEquals(expectedDepartement, actualDepartement);
        verify(departementService, times(1)).affectDepartementToEtudiants(idDepartement, etudiantIds);
    }

    @Test
    void testAssignDepartementToEtudiants_NullStudentList_NoException() {
        Integer departementId = 1;
        Departement expectedDepartement = new Departement();
        expectedDepartement.setIdDepart(departementId);

        // Mock the service to return the department without performing any assignment
        when(departementService.affectDepartementToEtudiants(departementId, null)).thenReturn(expectedDepartement);

        // Call the method with null etudiantIds
        Departement result = departementRestController.assignDepartementToEtudiants(departementId, null);

        // Verify that the department is returned without modifications
        assertNotNull(result);
        assertEquals(departementId, result.getIdDepart());
        verify(departementService, times(1)).affectDepartementToEtudiants(departementId, null);
    }


    @Test
    void testCountStudentsInDepartement() {
        Integer departementId = 1;
        long expectedCount = 5;

        // Mock the service call
        when(departementService.countStudentsInDepartement(departementId)).thenReturn(expectedCount);

        // Call the controller method
        long actualCount = departementRestController.countStudentsInDepartement(departementId);

        // Verify the result and service interaction
        assertEquals(expectedCount, actualCount);
        verify(departementService, times(1)).countStudentsInDepartement(departementId);
    }


}
