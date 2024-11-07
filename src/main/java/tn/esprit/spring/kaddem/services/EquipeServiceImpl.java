package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Equipe;

import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import javax.persistence.EntityNotFoundException;

import java.util.List;


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

	public Equipe retrieveEquipe(Integer equipeId){
		return equipeRepository.findById(equipeId)
                       .orElseThrow(() -> new EntityNotFoundException("Equipe with ID " + equipeId + " not found"));

	}

	public Equipe updateEquipe(Equipe e){
	return (	equipeRepository.save(e));
	}

	@Override
    public void evoluerEquipes() {
        // Placeholder implementation; replace with actual logic as needed
    }
	
}