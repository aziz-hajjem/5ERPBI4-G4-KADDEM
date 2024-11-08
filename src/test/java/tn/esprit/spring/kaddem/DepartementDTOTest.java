package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.kaddem.entities.DepartementDTO;
import static org.junit.jupiter.api.Assertions.*;

class DepartementDTOTest {
    @Test
    void testGettersAndSetters() {
        DepartementDTO departementDTO = new DepartementDTO();

        String nomDep = "Computer Science";



        // Tester les setters
        departementDTO.setNomDep(nomDep);


        assertEquals(nomDep, departementDTO.getNomDep());

    }
}