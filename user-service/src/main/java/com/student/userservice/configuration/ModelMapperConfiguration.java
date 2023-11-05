package com.student.userservice.configuration;

import com.student.api.dto.common.enums.Sex;
import com.student.api.dto.user.*;
import com.student.api.util.PersonalIdDataExtractor;
import com.student.api.util.TimeFormatParser;
import com.student.userservice.entity.*;
import org.modelmapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Time;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm = new ModelMapper();

        mm.typeMap(UserPersonalDetailsDto.class, UserEntity.class).addMappings(mapper -> {
            mapper.skip(UserPersonalDetailsDto::getId, UserEntity::setId);
            mapper.skip(UserPersonalDetailsDto::getRoles, UserEntity::setRoles);
        });

        mm.typeMap(AddressDto.class, AddressEntity.class).addMappings(mapper -> {
            mapper.skip(AddressEntity::setId);
            mapper.skip(AddressEntity::setUser);
        });

        mm.typeMap(CompetencyInformationDto.class, CompetencyInformationEntity.class).addMappings(mapper -> {
            mapper.skip(CompetencyInformationEntity::setId);
            mapper.skip(CompetencyInformationEntity::setDoctor);
        });

        mm.typeMap(ServiceTypeDto.class, ServiceTypeEntity.class).addMappings(mapper -> {
            mapper.skip(ServiceTypeEntity::setId);
            mapper.skip(ServiceTypeEntity::setDoctor);
            mapper.using((Converter<String, Time>) mappingContext -> TimeFormatParser
                    .parse(mappingContext.getSource())
            ).map(ServiceTypeDto::getDurationTime, ServiceTypeEntity::setDurationTime);
        });

        mm.typeMap(UserEntity.class, UserPersonalDetailsDto.class).addMappings(mapper -> {
            mapper.using((Converter<Set<UserRoleEntity>, Set<String>>) mappingContext -> mappingContext
                    .getSource().stream()
                    .map(UserRoleEntity::getRole)
                    .map(Enum::name)
                    .collect(Collectors.toSet())
            ).map(UserEntity::getRoles, UserPersonalDetailsDto::setRoles);
            mapper.using((Converter<String, Sex>) mappingContext -> PersonalIdDataExtractor
                    .getSex(mappingContext.getSource())
            ).map(UserEntity::getPersonalId, UserPersonalDetailsDto::setSex);
            mapper.using((Converter<String, String>) mappingContext -> PersonalIdDataExtractor
                    .getBirthDate(mappingContext.getSource(), PersonalIdDataExtractor.FORMATTER)
            ).map(UserEntity::getPersonalId, UserPersonalDetailsDto::setBirthDate);
        });

        mm.typeMap(UserEntity.class, DoctorDto.class).addMappings(mapper -> {
            mapper.using((Converter<String, Sex>) mappingContext -> PersonalIdDataExtractor
                    .getSex(mappingContext.getSource())
            ).map(UserEntity::getPersonalId, DoctorDto::setSex);
            mapper.using((Converter<String, Integer>) mappingContext -> PersonalIdDataExtractor
                    .getAge(mappingContext.getSource())
            ).map(UserEntity::getPersonalId, DoctorDto::setAge);
        });

        mm.typeMap(ServiceTypeEntity.class, ServiceTypeDto.class).addMappings(mapper -> {
            mapper.using((Converter<Time, String>) mappingContext -> mappingContext.getSource().toString()
            ).map(ServiceTypeEntity::getDurationTime, ServiceTypeDto::setDurationTime);
        });

        return mm;
    }

}
