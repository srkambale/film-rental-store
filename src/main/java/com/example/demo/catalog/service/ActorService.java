package com.example.demo.catalog.service;

import com.example.demo.catalog.dto.ActorDto;
import com.example.demo.catalog.entity.Actor;
import com.example.demo.catalog.repository.ActorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional(readOnly = true)
    public ActorDto getActorById(Long id) {
        return actorRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found"));
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found"));
        actor.setFirstName(dto.firstName());
        actor.setLastName(dto.lastName());
        return mapToDto(actorRepository.save(actor));
    }

    @Transactional
    public void deleteActor(Long id) {
        if (!actorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actor not found");
        }
        actorRepository.deleteById(id);
    }

    private ActorDto mapToDto(Actor actor) {
        return new ActorDto(actor.getActorId(), actor.getFirstName(), actor.getLastName());
    }
}
