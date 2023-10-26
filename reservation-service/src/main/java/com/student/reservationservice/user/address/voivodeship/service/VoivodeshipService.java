package com.student.reservationservice.user.address.voivodeship.service;

import com.student.reservationservice.user.address.voivodeship.entity.Voivodeship;
import com.student.reservationservice.user.address.voivodeship.exception.VoivodeshipNameExistsException;
import com.student.reservationservice.user.address.voivodeship.repository.VoivodeshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VoivodeshipService {
    private final VoivodeshipRepository voivodeshipRepository;

    @Autowired
    public VoivodeshipService(VoivodeshipRepository voivodeshipRepository) {
        this.voivodeshipRepository = voivodeshipRepository;
    }

    public Voivodeship addOrUpdateVoivodeship(Voivodeship voivodeship) {
        validateVoivodeshipNameUniqueness(voivodeship.getName());
        return voivodeshipRepository.save(voivodeship);
    }

    public List<Voivodeship> findAllVoivodeships() {
        return voivodeshipRepository.findAll();
    }

    public Optional<Voivodeship> findVoivodeshipById(int id) {
        return voivodeshipRepository.findVoivodeshipById(id);
    }

    public Optional<Voivodeship> findVoivodeshipByName(String name) {
        return voivodeshipRepository.findVoivodeshipByName(name);
    }

    @Transactional
    public void deleteVoivodeship(int id) {
        voivodeshipRepository.deleteVoivodeshipById(id);
    }

    private void validateVoivodeshipNameUniqueness(String name) {
        Optional<Voivodeship> voivodeship = findVoivodeshipByName(name);
        voivodeship.ifPresent(v -> {
            throw new VoivodeshipNameExistsException(name);
        });
    }
}
