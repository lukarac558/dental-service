package com.student.userservice.configuration;

import com.student.api.dto.user.AddressDto;
import com.student.api.dto.user.CompetencyInformationDto;
import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.dto.user.UserPersonalDetailsDto;
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
        });

        mm.typeMap(ServiceTypeEntity.class, ServiceTypeDto.class).addMappings(mapper -> {
            mapper.using((Converter<Time, String>) mappingContext -> mappingContext.getSource().toString()
            ).map(ServiceTypeEntity::getDurationTime, ServiceTypeDto::setDurationTime);
        });

        return mm;
    }

}
