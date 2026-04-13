package com.example.demo.catalog.controller;

import com.example.demo.catalog.dto.ActorDto;
import com.example.demo.catalog.service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("catalogActorController")
@RequestMapping("/api/v1/catalog/actors")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public List<ActorDto> getAllActors() {
        return actorService.getAllActors();
    }

    @GetMapping("/search")
    public List<ActorDto> searchActors(@RequestParam String name) {
        return actorService.searchActors(name);
    }

    @GetMapping("/{id}")
    public ActorDto getActorById(@PathVariable Long id) {
        return actorService.getActorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActorDto createActor(@RequestBody ActorDto actorDto) {
        return actorService.createActor(actorDto);
    }

    @PutMapping("/{id}")
    public ActorDto updateActor(@PathVariable Long id, @RequestBody ActorDto actorDto) {
        return actorService.updateActor(id, actorDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActor(@PathVariable Long id) {
        actorService.deleteActor(id);
    }
}
