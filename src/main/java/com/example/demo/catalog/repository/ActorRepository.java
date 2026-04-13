package com.example.demo.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.catalog.model.Actor;

import java.util.List;

@Repository("catalogActorRepository")
public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
