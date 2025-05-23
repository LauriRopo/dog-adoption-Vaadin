package com.example.application.repository;

import com.example.application.entity.Dog;
import com.example.application.entity.DogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

    @Query("SELECT d FROM Dog d LEFT JOIN FETCH d.trainings")
    List<Dog> findAllWithTrainings();
    List<Dog> findByStatus(DogStatus status);

}
