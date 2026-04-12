package com.example.demo.catalog.repository;

import com.example.demo.catalog.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("catalogActorRepository")
public interface ActorRepository extends JpaRepository<Actor, Long> {
}
