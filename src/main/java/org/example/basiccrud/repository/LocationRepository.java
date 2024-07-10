package org.example.basiccrud.repository;
import org.example.basiccrud.entity.Location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
