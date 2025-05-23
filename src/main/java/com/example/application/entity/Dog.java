package com.example.application.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;  // Many-to-One relationship with Location entity

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "dog_training",
            joinColumns = @JoinColumn(name = "dog_id"),
            inverseJoinColumns = @JoinColumn(name = "training_program_id")
    )
    private Set<TrainingProgram> trainings = new HashSet<>();  // Many-to-Many relationship with TrainingProgram entity

    private String name;
    private String breed;
    private int age;
    private String color;
    private boolean vaccinated;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private DogStatus status;

    // Getters and Setters
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

    public String getBreed() {
        return breed;
    }
    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }
    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<TrainingProgram> getTrainings() {
        return trainings;
    }

    public void setTrainings(Set<TrainingProgram> trainings) {
        this.trainings = trainings;
    }

    public DogStatus getStatus() {
        return status;
    }
    public void setStatus(DogStatus status) {
        this.status = status;
    }
}
