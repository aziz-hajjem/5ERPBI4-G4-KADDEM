package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class EquipeServiceImpl implements IEquipeService{
	EquipeRepository equipeRepository;


	public List<Equipe> retrieveAllEquipes(){
	return  (List<Equipe>) equipeRepository.findAll();
	}
	public Equipe addEquipe(Equipe e){
		return (equipeRepository.save(e));
	}

	public  void deleteEquipe(Integer idEquipe){
		Equipe e=retrieveEquipe(idEquipe);
		equipeRepository.delete(e);
	}

	public Equipe retrieveEquipe(Integer equipeId) {
    Optional<Equipe> optionalEquipe = equipeRepository.findById(equipeId);
    if (optionalEquipe.isPresent()) {
        return optionalEquipe.get();
    } else {
        // Handle the case when Equipe is not found, maybe throw an exception
        throw new EntityNotFoundException("Equipe with ID " + equipeId + " not found");
    }
}

	public Equipe updateEquipe(Equipe e){
	return (	equipeRepository.save(e));
	}

	public void evoluerEquipes() {
    List<Equipe> equipes = equipeRepository.findAll();
    
    for (Equipe equipe : equipes) {
        // Only process teams with JUNIOR or SENIOR level
        if (equipe.getNiveau() == Niveau.JUNIOR || equipe.getNiveau() == Niveau.SENIOR) {
            int nbEtudiantsAvecContratsActifs = 0;

            // Check each student in the team for active contracts
            for (Etudiant etudiant : equipe.getEtudiants()) {
                boolean hasActiveContract = false;

                // Check contracts for each student to see if there is an active contract
                for (Contrat contrat : etudiant.getContrats()) {
                    if (!contrat.getArchive()) { // Ensure contract is not archived
                        Date dateSysteme = new Date();
                        long differenceInTime = dateSysteme.getTime() - contrat.getDateFinContrat().getTime();
                        long differenceInYears = differenceInTime / (1000L * 60 * 60 * 24 * 365);

                        if (differenceInYears < 1) { // Contract is active within the past year
                            hasActiveContract = true;
                            break;
                        }
                    }
                }

                if (hasActiveContract) {
                    nbEtudiantsAvecContratsActifs++;
                }

                // If we have at least 3 students with active contracts, we can stop checking further
                if (nbEtudiantsAvecContratsActifs >= 3) {
                    break;
                }
            }

            // Upgrade the team level if it meets the criteria
            if (nbEtudiantsAvecContratsActifs >= 3) {
                if (equipe.getNiveau() == Niveau.JUNIOR) {
                    equipe.setNiveau(Niveau.SENIOR);
                } else if (equipe.getNiveau() == Niveau.SENIOR) {
                    equipe.setNiveau(Niveau.EXPERT);
                }
                equipeRepository.save(equipe);
            }
        }
    }
}

}