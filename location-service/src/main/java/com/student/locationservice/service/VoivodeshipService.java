package com.student.locationservice.service;

import com.student.api.exception.NotFoundException;
import com.student.locationservice.entity.VoivodeshipEntity;
import com.student.locationservice.repository.VoivodeshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.student.api.exception.ErrorConstants.VOIVODESHIP_ID_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class VoivodeshipService {
    private final VoivodeshipRepository voivodeshipRepository;

    public List<VoivodeshipEntity> findAllVoivodeships() {
        return voivodeshipRepository.findAll();
    }

    public VoivodeshipEntity findById(Long id) {
        return voivodeshipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(VOIVODESHIP_ID_NOT_FOUND_MESSAGE, id)));
    }
}
