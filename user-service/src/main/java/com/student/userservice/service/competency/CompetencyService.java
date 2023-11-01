package com.student.userservice.service.competency;

import com.student.api.annotation.extractor.auth.Info;
import com.student.api.exception.NotFoundException;
import com.student.api.exception.ObjectAlreadyExistsException;
import com.student.userservice.entity.CompetencyInformationEntity;
import com.student.userservice.entity.UserEntity;
import com.student.userservice.repository.CompetencyRepository;
import com.student.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.student.api.exception.ErrorConstants.*;

@Service
@RequiredArgsConstructor
public class CompetencyService {
    private final CompetencyRepository competencyRepository;
    private final UserRepository userRepository;

    public CompetencyInformationEntity findCompetencyInformation(Info info) {
        return competencyRepository.findByDoctor_Email(info.getEmail())
                .orElseThrow(() -> new NotFoundException(String.format(
                        COMPETENCY_INFORMATION_EMAIL_NOT_FOUND_MESSAGE,
                        info.getEmail()
                )));
    }

    @Transactional
    public CompetencyInformationEntity createCompetencyInformation(Info info, CompetencyInformationEntity competencyInformation) {
        UserEntity user = userRepository.findByEmail(info.getEmail())
                .orElseThrow(() -> new NotFoundException(String.format(
                        USER_BY_EMAIL_NOT_FOUND_MESSAGE,
                        info.getEmail()
                )));
        if(Objects.nonNull(user.getCompetencyInformation())) {
            throw new ObjectAlreadyExistsException(
                    String.format(COMPETENCY_INFORMATION_ALREADY_EXIST_MESSAGE, info.getEmail())
            );
        }
        competencyInformation.setDoctor(user);
        user.setCompetencyInformation(competencyInformation);
        return userRepository.save(user).getCompetencyInformation();
    }

    @Transactional
    public CompetencyInformationEntity updateCompetencyInformation(Info info, CompetencyInformationEntity competencyInformation) {
        CompetencyInformationEntity competency = findCompetencyInformation(info);
        competency.setTitle(competencyInformation.getTitle());
        competency.setDescription(competencyInformation.getDescription());
        return competencyRepository.save(competency);
    }
}
