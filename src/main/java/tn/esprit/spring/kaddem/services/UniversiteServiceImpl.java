package tn.esprit.spring.kaddem.services;


import org.springframework.stereotype.Service;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

@Service
public class UniversiteServiceImpl implements IUniversiteService {

    private static final String UNIVERSITE_NOT_FOUND = "Universite not found with id ";

    @Autowired
    private UniversiteRepository universiteRepository;

    
    private DepartementRepository departementRepository;

    public UniversiteServiceImpl() {
        // Constructor body can remain empty or be removed if no special initialization is required
    }

    public List<Universite> retrieveAllUniversites() {
        return (List<Universite>) universiteRepository.findAll();
    }

    public Universite addUniversite(Universite u) {
        return universiteRepository.save(u);
    }

    public Universite updateUniversite(Universite u) {
        return universiteRepository.save(u);
    }

    public Universite retrieveUniversite(Integer idUniversite) {
        return universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new EntityNotFoundException(UNIVERSITE_NOT_FOUND + idUniversite));
    }

    public void deleteUniversite(Integer idUniversite) {
        universiteRepository.delete(retrieveUniversite(idUniversite));
    }

    public void assignUniversiteToDepartement(Integer idUniversite, Integer idDepartement) {
        Universite u = universiteRepository.findById(idUniversite).orElse(null);
        Departement d = departementRepository.findById(idDepartement).orElse(null);

        if (u == null) {
            throw new EntityNotFoundException(UNIVERSITE_NOT_FOUND + idUniversite);
        }
        if (d == null) {
            throw new EntityNotFoundException("Departement not found with id " + idDepartement);
        }

        u.getDepartements().add(d);
        universiteRepository.save(u);
    }

    public Set<Departement> retrieveDepartementsByUniversite(Integer idUniversite) {
        Universite u = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new EntityNotFoundException(UNIVERSITE_NOT_FOUND + idUniversite));
        return u.getDepartements();
    }
}
