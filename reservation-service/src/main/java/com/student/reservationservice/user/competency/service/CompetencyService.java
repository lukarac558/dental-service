package com.student.reservationservice.user.competency.service;

import com.student.reservationservice.user.competency.entity.CompetencyInformation;
import com.student.reservationservice.user.competency.repository.CompetencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompetencyService {
    private final CompetencyRepository competencyRepository;

    @Autowired
    public CompetencyService(CompetencyRepository competencyRepository) {
        this.competencyRepository = competencyRepository;
    }

    public CompetencyInformation addOrUpdateCompetencyInformation(CompetencyInformation competencyInformation) {
        return competencyRepository.save(competencyInformation);
    }

    public Optional<CompetencyInformation> findCompetencyInformationById(Long id) {
        return competencyRepository.findCompetencyInformationById(id);
    }
}
