package com.example.demo.catalog.service;

import com.example.demo.catalog.dto.ActorDto;
import com.example.demo.catalog.dto.ActorUpdateDto;
import com.example.demo.catalog.entity.Actor;
import com.example.demo.catalog.repository.ActorRepository;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Transactional(readOnly = true)
    public List<ActorDto> getAllActors() {
        return actorRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ActorDto createActor(ActorDto dto) {
        Actor actor = new Actor();
        actor.setFirstName(dto.firstName());
        actor.setLastName(dto.lastName());
        actor = actorRepository.save(actor);
        return mapToDto(actor);
    }

    @Transactional
    public ActorDto updateActor(Long id, ActorDto dto) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found"));
        actor.setFirstName(dto.firstName());
        actor.setLastName(dto.lastName());
        return mapToDto(actorRepository.save(actor));
    }

    @Transactional
    public ActorDto patchActor(Long id, ActorUpdateDto updates) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found"));

        if (updates.getFirstName() != null) actor.setFirstName(updates.getFirstName());
        if (updates.getLastName() != null) actor.setLastName(updates.getLastName());

        return mapToDto(actorRepository.save(actor));
    }



    @Transactional(readOnly = true)
    public List<ActorDto> searchActors(String name) {
        return actorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ActorDto mapToDto(Actor actor) {
        return new ActorDto(actor.getActorId(), actor.getFirstName(), actor.getLastName());
    }
}
