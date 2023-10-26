package com.student.reservationservice.servicetype.service;

import com.student.reservationservice.calendar.entity.CalendarDay;
import com.student.reservationservice.servicetype.entity.ServiceType;
import com.student.reservationservice.servicetype.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TypeService {
    private final ServiceTypeRepository typeRepository;

    @Autowired
    public TypeService(ServiceTypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public ServiceType addOrUpdateType(ServiceType type) {
        return typeRepository.save(type);
    }

    public Optional<ServiceType> findTypeById(Long id) {
        return typeRepository.findServiceTypeById(id);
    }

    public List<ServiceType> findServiceTypesByUserId(Long userId) {
        return typeRepository.findServiceTypesByUserId(userId);
    }

    @Transactional
    public void deleteType(Long id) {
        typeRepository.deleteServiceTypeById(id);
    }
}
