package com.example.application.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class AdoptionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthday;
    private String occupation;

    @Lob
    private String about;

    @ManyToOne
    private Dog dog;

    // Getters ja setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }

    public Dog getDog() { return dog; }
    public void setDog(Dog dog) { this.dog = dog; }

    public String getPhoneNumber() {
        return phone;
    }
}
