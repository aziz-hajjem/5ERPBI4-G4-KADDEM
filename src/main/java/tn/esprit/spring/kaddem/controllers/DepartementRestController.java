package tn.esprit.spring.kaddem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.DepartementDTO;
import tn.esprit.spring.kaddem.services.IDepartementService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/departement")
public class DepartementRestController {
	IDepartementService departementService;
	// http://localhost:8089/Kaddem/departement/retrieve-all-departements
	@GetMapping("/retrieve-all-departements")
	public List<Departement> getDepartements() {
        return departementService.retrieveAllDepartements();
	}
	// http://localhost:8089/Kaddem/departement/retrieve-departement/8
	@GetMapping("/retrieve-departement/{departement-id}")
	public Departement retrieveDepartement(@PathVariable("departement-id") Integer departementId) {
		return departementService.retrieveDepartement(departementId);
	}


	// http://localhost:8089/Kaddem/departement/remove-departement/1
	@DeleteMapping("/remove-departement/{departement-id}")
	public void removeDepartement(@PathVariable("departement-id") Integer departementId) {
		departementService.deleteDepartement(departementId);
	}

	@PostMapping("/add-departement")
	public Departement addDepartement(@RequestBody DepartementDTO departementDTO) {
		Departement departement = new Departement();
		departement.setNomDepart(departementDTO.getNomDep()); // Conversion de String en Date

		return departementService.addDepartement(departement);
	}

	@PostMapping("/{departementId}/assign")
	public Departement assignDepartementToEtudiants(
			@PathVariable Integer departementId,
			@RequestBody List<Integer> etudiantIds) {
        return departementService.affectDepartementToEtudiants(departementId, etudiantIds);
	}

	@GetMapping("/{departementId}/students/count")
	public long countStudentsInDepartement(@PathVariable Integer departementId) {
		return departementService.countStudentsInDepartement(departementId);
	}



}


