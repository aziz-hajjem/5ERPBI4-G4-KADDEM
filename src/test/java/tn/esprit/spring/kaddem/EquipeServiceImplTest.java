import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        equipe.setNomEquipe("Test Team");
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
        assertEquals("Test Team", result.getNomEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
     void testDeleteEquipe() {
        when(equipeRepository.findById(equipe.getIdEquipe())).thenReturn(Optional.of(equipe));

        equipeService.deleteEquipe(equipe.getIdEquipe());
        verify(equipeRepository, times(1)).delete(equipe);
    }

    @Test
     void testDeleteEquipeNotFound() {
        when(equipeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> equipeService.deleteEquipe(999));
        verify(equipeRepository, times(1)).findById(999);
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
        when(equipeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> equipeService.retrieveEquipe(999));
        verify(equipeRepository, times(1)).findById(999);
    }

    @Test
     void testUpdateEquipe() {
        equipe.setNiveau(Niveau.SENIOR);
        when(equipeRepository.save(equipe)).thenReturn(equipe);

        Equipe result = equipeService.updateEquipe(equipe);
        assertNotNull(result);
        assertEquals(Niveau.SENIOR, result.getNiveau());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
public void testAddEquipeWithNullValues() {
    Equipe emptyEquipe = new Equipe();  // No fields set
    when(equipeRepository.save(emptyEquipe)).thenReturn(emptyEquipe);

    Equipe result = equipeService.addEquipe(emptyEquipe);
    assertNotNull(result);
    assertNull(result.getNomEquipe());
    verify(equipeRepository, times(1)).save(emptyEquipe);
}

@Test
public void testUpdateEquipeWithDifferentLevels() {
    Equipe updatedEquipe = new Equipe();
    updatedEquipe.setIdEquipe(1);
    updatedEquipe.setNomEquipe("Updated Team");
    updatedEquipe.setNiveau(Niveau.SENIOR);

    when(equipeRepository.save(updatedEquipe)).thenReturn(updatedEquipe);

    Equipe result = equipeService.updateEquipe(updatedEquipe);
    assertEquals("Updated Team", result.getNomEquipe());
    assertEquals(Niveau.SENIOR, result.getNiveau());
    verify(equipeRepository, times(1)).save(updatedEquipe);
}
@Test
public void testRetrieveAllEquipesEmpty() {
    when(equipeRepository.findAll()).thenReturn(Collections.emptyList());

    List<Equipe> result = equipeService.retrieveAllEquipes();
    assertTrue(result.isEmpty());
    verify(equipeRepository, times(1)).findAll();
}
@Test
public void testAddEquipeNull() {
    when(equipeRepository.save(null)).thenThrow(new IllegalArgumentException("Equipe cannot be null"));

    assertThrows(IllegalArgumentException.class, () -> equipeService.addEquipe(null));
    verify(equipeRepository, times(1)).save(null);
}
@Test
public void testUpdateEquipeNull() {
    assertThrows(IllegalArgumentException.class, () -> equipeService.updateEquipe(null));
}


}
