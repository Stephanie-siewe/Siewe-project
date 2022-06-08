package org.isj.ing.annuarium.webapp.mapper;

import org.isj.ing.annuarium.webapp.model.dto.ActeDto;
import org.isj.ing.annuarium.webapp.model.entities.Acte;
import org.mapstruct.*;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy =  NullValueCheckStrategy.ALWAYS)
public interface ActeMapper {

    Acte toEntity(ActeDto acteDto);

    ActeDto toDto(Acte acte);

    void copy(ActeDto acteDto, @MappingTarget Acte acte);
}
