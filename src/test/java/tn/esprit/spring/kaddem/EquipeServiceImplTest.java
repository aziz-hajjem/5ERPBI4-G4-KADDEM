import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EquipeServiceImplTest {

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;

    private Equipe equipe;

    @BeforeEach
    public void setup() {
        equipe = new Equipe();
        equipe.setIdEquipe(1);
        equipe.setNiveau(Niveau.JUNIOR);
    }

    @Test
    public void testRetrieveAllEquipes() {
        List<Equipe> equipes = Arrays.asList(equipe);
        when(equipeRepository.findAll()).thenReturn(equipes);

        List<Equipe> result = equipeService.retrieveAllEquipes();
        assertEquals(1, result.size());
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    public void testAddEquipe() {
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.addEquipe(equipe);
        assertNotNull(result);
        assertEquals(equipe.getIdEquipe(), result.getIdEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    public void testDeleteEquipe() {
        when(equipeRepository.findById(equipe.getIdEquipe())).thenReturn(Optional.of(equipe));

        equipeService.deleteEquipe(equipe.getIdEquipe());
        verify(equipeRepository, times(1)).delete(equipe);
    }

    @Test
    public void testRetrieveEquipe() {
        when(equipeRepository.findById(equipe.getIdEquipe())).thenReturn(Optional.of(equipe));

        Equipe result = equipeService.retrieveEquipe(equipe.getIdEquipe());
        assertNotNull(result);
        assertEquals(equipe.getIdEquipe(), result.getIdEquipe());
        verify(equipeRepository, times(1)).findById(equipe.getIdEquipe());
    }

    @Test
    public void testRetrieveEquipeNotFound() {
        when(equipeRepository.findById(equipe.getIdEquipe())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> equipeService.retrieveEquipe(equipe.getIdEquipe()));
        verify(equipeRepository, times(1)).findById(equipe.getIdEquipe());
    }

   
}
