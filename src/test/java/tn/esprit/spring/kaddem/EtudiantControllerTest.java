package tn.esprit.spring.kaddem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.controllers.EtudiantRestController;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.EtudiantDTO;
import tn.esprit.spring.kaddem.services.IEtudiantService;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EtudiantControllerTest {

    @Mock
    private IEtudiantService etudiantService;

    @InjectMocks
    private EtudiantRestController etudiantRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEtudiants() {
        Etudiant etudiant1 = new Etudiant();
        etudiant1.setIdEtudiant(1);
        Etudiant etudiant2 = new Etudiant();
        etudiant2.setIdEtudiant(2);
        List<Etudiant> expectedEtudiants = Arrays.asList(etudiant1, etudiant2);
        when(etudiantService.retrieveAllEtudiants()).thenReturn(expectedEtudiants);
        List<Etudiant> actualEtudiants = etudiantRestController.getEtudiants();
        assertEquals(expectedEtudiants, actualEtudiants);
        verify(etudiantService, times(1)).retrieveAllEtudiants();
    }

    @Test
    void testRetrieveEtudiant() {
        Integer etudiantId = 1;
        Etudiant expectedEtudiant = new Etudiant();
        expectedEtudiant.setIdEtudiant(etudiantId);
        when(etudiantService.retrieveEtudiant(etudiantId)).thenReturn(expectedEtudiant);
        Etudiant actualEtudiant = etudiantRestController.retrieveEtudiant(etudiantId);
        assertEquals(expectedEtudiant, actualEtudiant);
        verify(etudiantService, times(1)).retrieveEtudiant(etudiantId);
    }

    @Test
    void testRemoveEtudiant() {
        Integer etudiantId = 1;
        doNothing().when(etudiantService).removeEtudiant(etudiantId);
        etudiantRestController.removeEtudiant(etudiantId);
        verify(etudiantService, times(1)).removeEtudiant(etudiantId);
    }

    @Test
    void testAddEtudiant() {
        // Création de l'objet EtudiantDTO pour le test
        EtudiantDTO etudiantDTO = new EtudiantDTO();
        etudiantDTO.setNomE("Informatique");
        etudiantDTO.setPrenomE("Informatique");


        Etudiant expectedEtudiant = new Etudiant();
        expectedEtudiant.setNomE("Informatique");
        expectedEtudiant.setPrenomE("Informatique");



        when(etudiantService.addEtudiant(any(Etudiant.class))).thenReturn(expectedEtudiant);

        Etudiant result = etudiantRestController.addEtudiant(etudiantDTO);
        assertEquals(expectedEtudiant, result);
    }
}
