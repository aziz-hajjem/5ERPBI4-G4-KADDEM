package tn.esprit.spring.kaddem.services;

import tn.esprit.spring.kaddem.entities.Departement;

import java.util.List;

public interface IDepartementService {
    public List<Departement> retrieveAllDepartements();
    

    public  Departement retrieveDepartement (Integer idDepart);
    public  Departement addDepartement (Departement departement);
    public  void deleteDepartement(Integer idDepartement);
    public Departement affectDepartementToEtudiants(Integer departementId, List<Integer> etudiantIds);

    public void removeEtudiantFromDepartement(Integer etudiantId);

    long countStudentsInDepartement(Integer departementId);

}
