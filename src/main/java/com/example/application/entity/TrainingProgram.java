package com.example.application.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // Koulutusohjelman nimi
    private String description;  // Koulutusohjelman kuvaus

    @ManyToMany
    @JoinTable(
            name = "dog_training",  // Liittymätaulun nimi
            joinColumns = @JoinColumn(name = "training_program_id"),  // Viittaa TrainingProgram:iin
            inverseJoinColumns = @JoinColumn(name = "dog_id")  // Viittaa Dog:iin
    )
    private Set<Dog> dogs = new HashSet<>();  // Koirat, jotka osallistuvat tähän ohjelmaan

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(Set<Dog> dogs) {
        this.dogs = dogs;
    }
}
