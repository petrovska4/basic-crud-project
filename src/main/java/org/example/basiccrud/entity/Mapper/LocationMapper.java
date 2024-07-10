package org.example.basiccrud.entity.Mapper;

import org.example.basiccrud.entity.Location;
import org.example.basiccrud.entity.dto.LocationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(source = "id", target = "uuid")
    Location mapToLocation(LocationDto locationDto);

    @Mapping(source = "uuid", target = "id")
    LocationDto mapToLocationDto(Location location);
}
