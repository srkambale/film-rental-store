package com.example.demo.catalog.repository;

import com.example.demo.catalog.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("catalogActorRepository")
public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
