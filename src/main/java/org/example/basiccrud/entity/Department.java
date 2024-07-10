package org.example.basiccrud.entity;

import jakarta.persistence.*;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Department {
    @Id
    @GeneratedValue
    private UUID uuid;
    private String name;
}
