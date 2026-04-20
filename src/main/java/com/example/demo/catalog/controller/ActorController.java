package com.example.demo.catalog.controller;

import com.example.demo.catalog.dto.ActorDto;
import com.example.demo.catalog.dto.ActorUpdateDto;
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



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActorDto createActor(@RequestBody ActorDto actorDto) {
        return actorService.createActor(actorDto);
    }

    @PutMapping("/{id}")
    public ActorDto updateActor(@PathVariable Long id, @RequestBody ActorDto actorDto) {
        return actorService.updateActor(id, actorDto);
    }

    @PatchMapping("/{id}")
    public ActorDto patchActor(@PathVariable Long id, @RequestBody ActorUpdateDto updates) {
        return actorService.patchActor(id, updates);
    }


}
