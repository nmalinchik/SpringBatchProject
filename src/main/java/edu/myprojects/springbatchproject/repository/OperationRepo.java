package edu.myprojects.springbatchproject.repository;

import edu.myprojects.springbatchproject.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationRepo extends JpaRepository<Operation, Long> {

    Optional<Operation> findByNameAndValueAndStatus(String name, String value, String status);

}
