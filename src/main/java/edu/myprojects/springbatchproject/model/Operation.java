package edu.myprojects.springbatchproject.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public @Data class Operation {

    @Id
    private long id;
    private String name;
    private String value;
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return id == operation.id &&
                name.equals(operation.name) &&
                value.equals(operation.value) &&
                status.equals(operation.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, value, status);
    }
}
