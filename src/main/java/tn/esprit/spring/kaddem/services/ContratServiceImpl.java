package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Slf4j
@Service
public class ContratServiceImpl implements IContratService{
	    @Autowired
		ContratRepository contratRepository;
		@Autowired
		EtudiantRepository etudiantRepository;
	public List<Contrat> retrieveAllContrats(){
		return  contratRepository.findAll();
	}

	public Contrat updateContrat (Contrat  ce){
		return contratRepository.save(ce);
	}

	public  Contrat addContrat (Contrat ce){
		return contratRepository.save(ce);
	}

	public Contrat retrieveContrat (Integer  idContrat){
		return contratRepository.findById(idContrat).orElse(null);
	}

	public  void removeContrat(Integer idContrat){
		Contrat c=retrieveContrat(idContrat);
		contratRepository.delete(c);
	}



	public Contrat affectContratToEtudiant(Integer idContrat, String nomE, String prenomE) {
    Etudiant e = etudiantRepository.findByNomEAndPrenomE(nomE, prenomE);
    Contrat ce = contratRepository.findByIdContrat(idContrat);
    Set<Contrat> contrats = e.getContrats();
    int nbContratssActifs = 0;

    for (Contrat contrat : contrats) {
        if (!Boolean.TRUE.equals(contrat.getArchive())) {
            nbContratssActifs++;
        }
    }

    if (nbContratssActifs < 4) {
        ce.setEtudiant(e);
        contratRepository.save(ce);
    }
    return ce;
}

	public 	Integer nbContratsValides(Date startDate, Date endDate){
		return contratRepository.getnbContratsValides(startDate, endDate);
	}

	public void retrieveAndUpdateStatusContrat(){
    List<Contrat> contrats = contratRepository.findAll();
    List<Contrat> contrats15j = new ArrayList<>(); // Initialize as an empty list
    List<Contrat> contratsAarchiver = new ArrayList<>(); // Initialize as an empty list
    for (Contrat contrat : contrats) {
        Date dateSysteme = new Date();
        if (Boolean.FALSE.equals(contrat.getArchive())) {
            long differenceInTime = dateSysteme.getTime() - contrat.getDateFinContrat().getTime();
            long differenceInDays = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;
            if (differenceInDays == 15) {
                contrats15j.add(contrat);
                log.info(" Contrat : " + contrat);
            }
            if (differenceInDays == 0) {
                contratsAarchiver.add(contrat);
                contrat.setArchive(true);
                contratRepository.save(contrat);
            }
        }
    }
}

	public float getChiffreAffaireEntreDeuxDates(Date startDate, Date endDate){
		float differenceInTime = endDate.getTime() - startDate.getTime();
		float differenceInDays = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;
		float differenceInMonths =differenceInDays/30;
        List<Contrat> contrats=contratRepository.findAll();
		float chiffreAffaireEntreDeuxDates=0;
		for (Contrat contrat : contrats) {
			if (contrat.getSpecialite()== Specialite.IA){
				chiffreAffaireEntreDeuxDates+=(differenceInMonths*300);
			} else if (contrat.getSpecialite()== Specialite.CLOUD) {
				chiffreAffaireEntreDeuxDates+=(differenceInMonths*400);
			}
			else if (contrat.getSpecialite()== Specialite.RESEAUX) {
				chiffreAffaireEntreDeuxDates+=(differenceInMonths*350);
			}
			else 
			 {
				 chiffreAffaireEntreDeuxDates+=(differenceInMonths*450);
			}
		}
		return chiffreAffaireEntreDeuxDates;


	}


}
