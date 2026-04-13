package com.filmrental.catalog.repository;

import com.filmrental.catalog.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

    List<Actor> findByLastNameContainingIgnoreCase(String lastName);

    List<Actor> findByFirstNameContainingIgnoreCase(String firstName);
}