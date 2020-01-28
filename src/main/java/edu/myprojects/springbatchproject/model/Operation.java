package edu.myprojects.springbatchproject.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public @Data class Operation {

    @Id
    private long id;
    private String name;
    private String value;
    private String status;
}
