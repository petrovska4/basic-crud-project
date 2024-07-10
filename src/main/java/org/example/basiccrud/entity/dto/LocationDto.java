package org.example.basiccrud.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.basiccrud.entity.Department;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private String id;
    private String name;
    private Department department;
}
