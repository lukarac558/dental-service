package com.student.userservice.service.user;

import com.student.api.annotation.extractor.auth.Info;
import com.student.api.dto.common.enums.Role;
import com.student.api.exception.*;
import com.student.api.dto.user.DoctorSearchRequestDto;
import com.student.userservice.entity.UserEntity;
import com.student.userservice.entity.UserRoleEntity;
import com.student.userservice.repository.UserRepository;
import com.student.userservice.client.VoivodeshipClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.student.api.exception.handler.ErrorConstants.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final VoivodeshipClient voivodeshipClient;
    private final UserRepository userRepository;

    public UserEntity findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)));
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format(USER_BY_EMAIL_NOT_FOUND_MESSAGE, email)));
    }

    @Transactional
    public void deleteUser(Info info) {
        userRepository.deleteByEmail(info.getEmail());
    }

    @Transactional
    public UserEntity createIfNotPresent(Info info, UserEntity user) {
        if(userRepository.findByEmail(info.getEmail()).isEmpty()) {
            voivodeshipClient.getVoivodeship(user.getAddress().getVoivodeshipId());
            user.setEmail(info.getEmail());
            user.getAddress().setUser(user);
            info.getRoles().forEach(role -> {
                UserRoleEntity userRole = new UserRoleEntity();
                userRole.setRole(role);
                userRole.setUser(user);
                user.getRoles().add(userRole);
            });

            return userRepository.save(user);
        }
        throw new ObjectAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS_MESSAGE, info.getEmail()));
    }

    public Page<UserEntity> findDoctor(DoctorSearchRequestDto doctorSearch) {
        return userRepository.findAll(
                new DoctorSpecification(doctorSearch),
                doctorSearch.pageable()
        );
    }

    @Transactional
    public UserEntity updateUser(Info info, UserEntity user) {
        voivodeshipClient.getVoivodeship(user.getAddress().getVoivodeshipId());
        UserEntity userEntity = findUserByEmail(info.getEmail());
        userEntity.setName(user.getName());
        userEntity.setSurname(user.getSurname());
        userEntity.setPhoneNumber(user.getPhoneNumber());
        userEntity.setPersonalId(user.getPersonalId());
        userEntity.getAddress().setVoivodeshipId(user.getAddress().getVoivodeshipId());
        userEntity.getAddress().setCity(user.getAddress().getCity());
        userEntity.getAddress().setStreet(user.getAddress().getStreet());
        userEntity.getAddress().setBuildingNumber(user.getAddress().getBuildingNumber());
        userEntity.getAddress().setPostalCode(user.getAddress().getPostalCode());
        return userRepository.save(userEntity);
    }

    public UserEntity findDoctorById(Long id) {
        return userRepository.findByIdAndRoles_Role(id, Role.DOCTOR)
                        .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)));
    }
}
