package tn.esprit.spring.kaddem.services;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class ContratServiceImpl implements IContratService{

	private final ContratRepository contratRepository;
	private final EtudiantRepository etudiantRepository;


	public ContratServiceImpl(ContratRepository contratRepository, EtudiantRepository etudiantRepository) {
		this.contratRepository = contratRepository;
		this.etudiantRepository = etudiantRepository;
	}

	public List<Contrat> retrieveAllContrats(){

		log.info("affichage avec succées");
		return  contratRepository.findAll();
	}

	public Contrat retrieveContrat (Integer  idContrat){

		log.info("affichage avec succées ");
		return contratRepository.findById(idContrat).orElse(null);
	}

	public  void removeContrat(Integer idContrat){
		Contrat c=retrieveContrat(idContrat);
		contratRepository.delete(c);
		log.info("le contart a ete supprimer ");
	}


	public Contrat affectContratToEtudiant(Integer idContrat, String nomE, String prenomE) {
		Etudiant e = etudiantRepository.findByNomEAndPrenomE(nomE, prenomE);
		Contrat ce = contratRepository.findByIdContrat(idContrat);
		Set<Contrat> contrats = e.getContrats();
		Integer nbContratssActifs = 0;
		if (!contrats.isEmpty()) {  // Use isEmpty() to check if the collection is empty
			for (Contrat contrat : contrats) {
				if (Boolean.TRUE.equals(contrat.getArchive())) {  // Remove the unnecessary boolean literal
					nbContratssActifs++;
				}
			}
		}

		if (nbContratssActifs <= 4) {  // Covered code
			ce.setEtudiant(e);
			log.info("l'affectation sera faite");
			contratRepository.save(ce);
		}

		return ce;
	}



	public void retrieveAndUpdateStatusContrat() {
		List<Contrat> contrats = contratRepository.findAll();
		List<Contrat> contrats15j = new ArrayList<>(); // Initialize the list
		List<Contrat> contratsAarchiver = new ArrayList<>(); // Initialize the list

		for (Contrat contrat : contrats) {
			Date dateSysteme = new Date();
			if (!contrat.getArchive()) {
				long differenceInTime = dateSysteme.getTime() - contrat.getDateFinContrat().getTime();
				long differenceInDays = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;

				if (differenceInDays == 15) {
					contrats15j.add(contrat);
					log.info("Contrat : " + contrat);
				}
				if (differenceInDays == 0) {
					contratsAarchiver.add(contrat);
					contrat.setArchive(true);
					contratRepository.save(contrat);
				}
			}
		}
	}

	public Contrat updateContrat (Contrat  ce){
		log.info("la mise a jour  sera faite");
		return contratRepository.save(ce);
	}

	public  Contrat addContrat (Contrat ce){
		log.info("l'ajout sera faite");
		return contratRepository.save(ce);
	}

	public 	Integer nbContratsValides(Date startDate, Date endDate){
		log.info("le calcule sera fait");
		return contratRepository.getnbContratsValides(startDate, endDate);
	}

	public float getChiffreAffaireEntreDeuxDates(Date startDate, Date endDate) {
		float chiffreAffaireEntreDeuxDates = 0;

		try {
			float differenceInTime =  (endDate.getTime() - startDate.getTime());
			float differenceInDays = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;
			float differenceInMonths = differenceInDays / 30;

			List<Contrat> contrats = contratRepository.findAll();
			if (contrats.isEmpty()) {
				log.info("Aucun contrat trouvé.");
				return 0;
			}

			for (Contrat contrat : contrats) {
				if (contrat.getSpecialite() == Specialite.IA) {
					chiffreAffaireEntreDeuxDates += (differenceInMonths * 300);
				} else if (contrat.getSpecialite() == Specialite.CLOUD) {
					chiffreAffaireEntreDeuxDates += (differenceInMonths * 400);
				} else if (contrat.getSpecialite() == Specialite.RESEAUX) {
					chiffreAffaireEntreDeuxDates += (differenceInMonths * 350);
				} else {
					chiffreAffaireEntreDeuxDates += (differenceInMonths * 450);
				}
			}

			log.info("Calcul du chiffre d'affaires effectué avec succès : {}", chiffreAffaireEntreDeuxDates);
		} catch (Exception e) {
			log.error("Erreur lors du calcul du chiffre d'affaires : ", e);
			return 0;
		}

		return chiffreAffaireEntreDeuxDates;
	}



}