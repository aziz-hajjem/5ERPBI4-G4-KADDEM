package tn.esprit.spring.kaddem.services;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.List;

@Service
@Slf4j
public class EtudiantServiceImpl implements IEtudiantService{
	final
	EtudiantRepository etudiantRepository ;

    final
	DepartementRepository departementRepository;

	public EtudiantServiceImpl(EtudiantRepository etudiantRepository, DepartementRepository departementRepository) {
		this.etudiantRepository = etudiantRepository;
		this.departementRepository = departementRepository;
	}

	public List<Etudiant> retrieveAllEtudiants(){
	return (List<Etudiant>) etudiantRepository.findAll();
	}

	public Etudiant addEtudiant (Etudiant e){
		return etudiantRepository.save(e);
	}


	public Etudiant retrieveEtudiant(Integer  idEtudiant){
		return etudiantRepository.findById(idEtudiant).orElse(null);
	}

	public void removeEtudiant(Integer idEtudiant){
	Etudiant e=retrieveEtudiant(idEtudiant);
	etudiantRepository.delete(e);
	}


}
