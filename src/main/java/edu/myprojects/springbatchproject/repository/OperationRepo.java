package edu.myprojects.springbatchproject.repository;

import edu.myprojects.springbatchproject.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepo extends JpaRepository<Operation, Long> {
}
