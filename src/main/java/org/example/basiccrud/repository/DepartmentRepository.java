package org.example.basiccrud.repository;
import org.example.basiccrud.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    Department findByName(String name);
}
