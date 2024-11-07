import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

@ExtendWith(MockitoExtension.class)
 class EquipeServiceImplTest {

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;

    private Equipe equipe;

    @BeforeEach
     void setup() {
        equipe = new Equipe();
        equipe.setIdEquipe(1);
        equipe.setNiveau(Niveau.JUNIOR);
    }

    @Test
     void testRetrieveAllEquipes() {
        List<Equipe> equipes = Arrays.asList(equipe);
        when(equipeRepository.findAll()).thenReturn(equipes);

        List<Equipe> result = equipeService.retrieveAllEquipes();
        assertEquals(1, result.size());
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
     void testAddEquipe() {
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.addEquipe(equipe);
        assertNotNull(result);
        assertEquals(equipe.getIdEquipe(), result.getIdEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
     void testDeleteEquipe() {
        when(equipeRepository.findById(equipe.getIdEquipe())).thenReturn(Optional.of(equipe));

        equipeService.deleteEquipe(equipe.getIdEquipe());
        verify(equipeRepository, times(1)).delete(equipe);
    }

    @Test
     void testRetrieveEquipe() {
        when(equipeRepository.findById(equipe.getIdEquipe())).thenReturn(Optional.of(equipe));

        Equipe result = equipeService.retrieveEquipe(equipe.getIdEquipe());
        assertNotNull(result);
        assertEquals(equipe.getIdEquipe(), result.getIdEquipe());
        verify(equipeRepository, times(1)).findById(equipe.getIdEquipe());
    }

    @Test
     void testRetrieveEquipeNotFound() {
        when(equipeRepository.findById(equipe.getIdEquipe())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> equipeService.retrieveEquipe(equipe.getIdEquipe()));
        verify(equipeRepository, times(1)).findById(equipe.getIdEquipe());
    }

    @Test
public void testRetrieveEquipeWithInvalidId() {
    when(equipeRepository.findById(anyInt())).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> equipeService.retrieveEquipe(999));
    verify(equipeRepository, times(1)).findById(999);
}

@Test
public void testDeleteEquipeWithInvalidId() {
    when(equipeRepository.findById(anyInt())).thenReturn(Optional.empty());
    assertThrows(EntityNotFoundException.class, () -> equipeService.deleteEquipe(999));
    verify(equipeRepository, times(1)).findById(999);
}

@Test
public void testEvoluerEquipesWithNoStudents() {
    equipe.setEtudiants(Collections.emptySet());
    List<Equipe> equipes = Collections.singletonList(equipe);

    when(equipeRepository.findAll()).thenReturn(equipes);
    equipeService.evoluerEquipes();

    assertEquals(Niveau.JUNIOR, equipe.getNiveau());  // Expect no change in level
    verify(equipeRepository, times(1)).findAll();
    verify(equipeRepository, times(0)).save(equipe);  // No save should occur
}

@Test
public void testEvoluerEquipesWithArchivedContracts() {
    Contrat contrat = new Contrat();
    contrat.setArchive(true);  // Archived contract
    contrat.setDateFinContrat(new Date(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 365 * 2)));

    Etudiant etudiant = new Etudiant();
    etudiant.setContrats(Collections.singleton(contrat));

    Set<Etudiant> etudiants = new HashSet<>();
    etudiants.add(etudiant);
    equipe.setEtudiants(etudiants);

    List<Equipe> equipes = Collections.singletonList(equipe);
    when(equipeRepository.findAll()).thenReturn(equipes);

    equipeService.evoluerEquipes();
    assertEquals(Niveau.JUNIOR, equipe.getNiveau());  // Expect no change in level
    verify(equipeRepository, times(1)).findAll();
    verify(equipeRepository, times(0)).save(equipe);  // No save should occur since contract is archived
}

@Test
public void testEvoluerEquipesWithLessThanThreeActiveContracts() {
    Contrat contrat = new Contrat();
    contrat.setArchive(false);
    contrat.setDateFinContrat(new Date(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 365 * 2))); // 2 years ago

    Etudiant etudiant = new Etudiant();
    etudiant.setContrats(Collections.singleton(contrat));

    Set<Etudiant> etudiants = new HashSet<>();
    etudiants.add(etudiant);
    equipe.setEtudiants(etudiants);

    List<Equipe> equipes = Collections.singletonList(equipe);
    when(equipeRepository.findAll()).thenReturn(equipes);

    equipeService.evoluerEquipes();

    assertEquals(Niveau.JUNIOR, equipe.getNiveau());  // No change expected due to insufficient contracts
    verify(equipeRepository, times(1)).findAll();
    verify(equipeRepository, times(0)).save(equipe);  // No save should occur
}


   
}
