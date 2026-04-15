package com.filmrental.catalog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.filmrental.catalog.dto.ActorDto;
import com.filmrental.catalog.entity.Actor;
import com.filmrental.catalog.repository.ActorRepository;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<ActorDto> getAllActors() {
        List<Actor> actors = actorRepository.findAll();
        List<ActorDto> result = new ArrayList<>();
        for (Actor actor : actors) {
            result.add(mapToDto(actor));
        }
        return result;
    }

    public ActorDto getActorById(Integer id) {
        Actor actor = actorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
        return mapToDto(actor);
    }

    public List<ActorDto> searchByLastName(String lastName) {
        List<Actor> actors = actorRepository.findByLastNameContainingIgnoreCase(lastName);
        List<ActorDto> result = new ArrayList<>();
        for (Actor actor : actors) {
            result.add(mapToDto(actor));
        }
        return result;
    }

    public ActorDto createActor(ActorDto dto) {
        Actor actor = new Actor();
        actor.setFirstName(dto.getFirstName());
        actor.setLastName(dto.getLastName());
        actor.setLastUpdate(LocalDateTime.now());
        return mapToDto(actorRepository.save(actor));
    }

    public ActorDto updateActor(Integer id, ActorDto dto) {
        Actor actor = actorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
        actor.setFirstName(dto.getFirstName());
        actor.setLastName(dto.getLastName());
        actor.setLastUpdate(LocalDateTime.now());
        return mapToDto(actorRepository.save(actor));
    }

    public void deleteActor(Integer id) {
        if (!actorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Actor not found with id: " + id);
        }
        actorRepository.deleteById(id);
    }

    private ActorDto mapToDto(Actor actor) {
        return new ActorDto(actor.getActorId(), actor.getFirstName(), actor.getLastName());
    }
}