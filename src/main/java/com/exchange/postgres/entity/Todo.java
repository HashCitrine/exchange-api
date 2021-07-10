package com.exchange.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Todo {

    @Id
    private Long id;

    private String description;

    private String details;

    private boolean done;
}
