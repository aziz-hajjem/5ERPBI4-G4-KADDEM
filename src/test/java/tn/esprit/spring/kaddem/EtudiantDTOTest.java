package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.kaddem.entities.EtudiantDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EtudiantDTOTest {
    @Test
    void testGettersAndSetters() {
        EtudiantDTO etudiantDTO = new EtudiantDTO();

        String nomE = "Zouari";
        String prenomE = "Aymen";



        etudiantDTO.setNomE(nomE);
        etudiantDTO.setPrenomE(prenomE);


        assertEquals(nomE, etudiantDTO.getNomE());
        assertEquals(prenomE, etudiantDTO.getPrenomE());

    }
}