package tn.esprit.spring.kaddem.controllers;

import org.springframework.web.bind.annotation.*;

import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.EtudiantDTO;
import tn.esprit.spring.kaddem.services.IEtudiantService;

import java.util.List;

@RestController
@RequestMapping("/etudiant")
public class EtudiantRestController {
	final
	IEtudiantService etudiantService;

	public EtudiantRestController(IEtudiantService etudiantService) {
		this.etudiantService = etudiantService;
	}

	// http://localhost:8089/Kaddem/etudiant/retrieve-all-etudiants
	@GetMapping("/retrieve-all-etudiants")
	public List<Etudiant> getEtudiants() {
        return etudiantService.retrieveAllEtudiants();
	}
	// http://localhost:8089/Kaddem/etudiant/retrieve-etudiant/8
	@GetMapping("/retrieve-etudiant/{etudiant-id}")
	public Etudiant retrieveEtudiant(@PathVariable("etudiant-id") Integer etudiantId) {
		return etudiantService.retrieveEtudiant(etudiantId);
	}

	// http://localhost:8089/Kaddem/etudiant/add-etudiant
	@PostMapping("/add-etudiant")
	public Etudiant addEtudiant(@RequestBody EtudiantDTO etudiantDTO) {
		Etudiant etudiant = new Etudiant();
		etudiant.setNomE(etudiantDTO.getNomE()); // Conversion de String en Date
		etudiant.setPrenomE(etudiantDTO.getPrenomE());
		return etudiantService.addEtudiant(etudiant);
	}


	// http://localhost:8089/Kaddem/etudiant/remove-etudiant/1
	@DeleteMapping("/remove-etudiant/{etudiant-id}")
	public void removeEtudiant(@PathVariable("etudiant-id") Integer etudiantId) {
		etudiantService.removeEtudiant(etudiantId);
	}
}


