package com.example.application;

import com.example.application.entity.Dog;
import com.example.application.entity.Location;
import com.example.application.entity.TrainingProgram;
import com.example.application.repository.DogRepository;
import com.example.application.repository.LocationRepository;
import com.example.application.repository.TrainingProgramRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
    //Uusin  versio