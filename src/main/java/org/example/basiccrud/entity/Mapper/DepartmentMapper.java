package org.example.basiccrud.entity.Mapper;

import org.example.basiccrud.entity.Department;
import org.example.basiccrud.entity.dto.DepartmentDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    @Mapping(source = "id", target = "uuid", qualifiedByName = "mapIdToUuid")
    Department mapToDepartment(DepartmentDto departmentDto);

    @Mapping(source = "uuid", target = "id", qualifiedByName = "mapUuidToId")
    DepartmentDto mapToDepartmentDto(Department department);

    @Named("mapIdToUuid")
    public static UUID mapIdToUuid(String id){
        return UUID.fromString(id);
    }

    @Named("mapUuidToId")
    public static String mapUuidToId(UUID id){
        return id.toString();
    }

}
