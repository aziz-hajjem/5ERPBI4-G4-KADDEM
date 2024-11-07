package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UniversiteServiceImplTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private UniversiteServiceImpl universiteService;

    private Universite universite;
    private Departement departement;

    @BeforeEach
    public void setUp() {
        universite = new Universite();
        universite.setId(1);

        departement = new Departement();
        departement.setId(1);
    }

    @Test
    public void testRetrieveAllUniversites() {
        when(universiteRepository.findAll()).thenReturn(Collections.singletonList(universite));
        
        List<Universite> result = universiteService.retrieveAllUniversites();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    public void testAddUniversite() {
        when(universiteRepository.save(universite)).thenReturn(universite);
        
        Universite result = universiteService.addUniversite(universite);
        
        assertNotNull(result);
        assertEquals(universite, result);
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    public void testUpdateUniversite() {
        when(universiteRepository.save(universite)).thenReturn(universite);
        
        Universite result = universiteService.updateUniversite(universite);
        
        assertNotNull(result);
        assertEquals(universite, result);
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    public void testRetrieveUniversiteExists() {
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        
        Universite result = universiteService.retrieveUniversite(1);
        
        assertNotNull(result);
        assertEquals(universite, result);
        verify(universiteRepository, times(1)).findById(1);
    }

    @Test
    public void testRetrieveUniversiteNotFound() {
        when(universiteRepository.findById(1)).thenReturn(Optional.empty());
        
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            universiteService.retrieveUniversite(1);
        });
        
        assertEquals("Universite not found with id 1", exception.getMessage());
        verify(universiteRepository, times(1)).findById(1);
    }

    @Test
    public void testDeleteUniversite() {
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        
        universiteService.deleteUniversite(1);
        
        verify(universiteRepository, times(1)).delete(universite);
    }

    @Test
    public void testAssignUniversiteToDepartement() {
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement));
        
        universiteService.assignUniversiteToDepartement(1, 1);
        
        assertTrue(universite.getDepartements().contains(departement));
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    public void testAssignUniversiteToDepartementUniversiteNotFound() {
        when(universiteRepository.findById(1)).thenReturn(Optional.empty());
        
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            universiteService.assignUniversiteToDepartement(1, 1);
        });
        
        assertEquals("Universite not found with id 1", exception.getMessage());
        verify(universiteRepository, never()).save(any(Universite.class));
    }

    @Test
    public void testAssignUniversiteToDepartementDepartementNotFound() {
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        when(departementRepository.findById(1)).thenReturn(Optional.empty());
        
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            universiteService.assignUniversiteToDepartement(1, 1);
        });
        
        assertEquals("Departement not found with id 1", exception.getMessage());
        verify(universiteRepository, never()).save(any(Universite.class));
    }

    @Test
    public void testRetrieveDepartementsByUniversite() {
        universite.setDepartements(Set.of(departement));
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        
        Set<Departement> result = universiteService.retrieveDepartementsByUniversite(1);
        
        assertNotNull(result);
        assertTrue(result.contains(departement));
        verify(universiteRepository, times(1)).findById(1);
    }

    @Test
    public void testRetrieveDepartementsByUniversiteNotFound() {
        when(universiteRepository.findById(1)).thenReturn(Optional.empty());
        
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            universiteService.retrieveDepartementsByUniversite(1);
        });
        
        assertEquals("Universite not found with id 1", exception.getMessage());
        verify(universiteRepository, times(1)).findById(1);
    }
}
