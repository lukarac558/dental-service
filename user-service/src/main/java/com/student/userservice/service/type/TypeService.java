package com.student.userservice.service.type;

import com.student.api.annotation.extractor.auth.Info;
import com.student.api.dto.user.ServiceTypeSearchRequestDto;
import com.student.api.exception.NotFoundException;
import com.student.userservice.entity.ServiceTypeEntity;
import com.student.userservice.entity.UserEntity;
import com.student.userservice.repository.ServiceTypeRepository;
import com.student.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.student.api.exception.handler.ErrorConstants.SERVICE_TYPE_NOT_FOUND_MESSAGE;
import static com.student.api.exception.handler.ErrorConstants.USER_BY_EMAIL_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class TypeService {
    private final ServiceTypeRepository typeRepository;
    private final UserRepository userRepository;

    @Transactional
    public ServiceTypeEntity createType(ServiceTypeEntity type, Info info) {
        UserEntity user = userRepository.findByEmail(info.getEmail())
                .orElseThrow(() -> new NotFoundException(String.format(
                        USER_BY_EMAIL_NOT_FOUND_MESSAGE,
                        info.getEmail()
                )));
        type.setDoctor(user);
        return typeRepository.save(type);
    }

    @Transactional
    public ServiceTypeEntity updateType(ServiceTypeEntity type, Info info) {
        ServiceTypeEntity serviceType = typeRepository.findByIdAndDoctor_Email(type.getId(), info.getEmail())
                .orElseThrow(() -> new NotFoundException(String.format(
                        SERVICE_TYPE_NOT_FOUND_MESSAGE,
                        type.getId()
                )));

        serviceType.setName(type.getName());
        serviceType.setDurationTime(type.getDurationTime());
        serviceType.setDescription(type.getDescription());

        return typeRepository.save(serviceType);
    }

    public List<ServiceTypeEntity> findTypesByIds(List<Long> ids) {
        return ids.stream()
                .map(this::findTypeById)
                .collect(Collectors.toList());
    }

    public List<ServiceTypeEntity> findTypeByDoctorId(Long doctorId) {
        return typeRepository.findServiceTypeEntitiesByDoctor_Id(doctorId);
    }

    public ServiceTypeEntity findTypeById(Long id) {
        return typeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(SERVICE_TYPE_NOT_FOUND_MESSAGE, id)));
    }

    public Page<ServiceTypeEntity> findAll(Info info, ServiceTypeSearchRequestDto searchRequestDto) {
        return typeRepository.findAll(
                new TypeSpecification(info, searchRequestDto),
                searchRequestDto.pageable()
        );
    }

    @Transactional
    public void deleteType(Info info, Long id) {
        typeRepository.deleteByIdAndDoctor_Email(id, info.getEmail());
    }

}
