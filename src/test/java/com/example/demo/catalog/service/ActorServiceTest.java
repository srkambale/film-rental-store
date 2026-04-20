package com.example.demo.catalog.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.catalog.dto.ActorDto;
import com.example.demo.catalog.entity.Actor;
import com.example.demo.catalog.repository.ActorRepository;
import com.example.demo.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorService actorService;

    private Actor actor;
    private ActorDto actorDto;

    @BeforeEach
    void setUp() {
        actor = new Actor();
        actor.setActorId(1L);
        actor.setFirstName("JOHN");
        actor.setLastName("DOE");

        actorDto = new ActorDto(1L, "JOHN", "DOE");
    }

    @Test
    void testGetAllActors() {
        when(actorRepository.findAll()).thenReturn(List.of(actor));

        List<ActorDto> result = actorService.getAllActors();

        assertEquals(1, result.size());
        assertEquals("JOHN", result.get(0).firstName());
        verify(actorRepository, times(1)).findAll();
    }

    @Test
    void testGetActorById_Success() {
        when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));

        ActorDto result = actorService.getActorById(1L);

        assertNotNull(result);
        assertEquals("DOE", result.lastName());
    }

    @Test
    void testGetActorById_NotFound() {
        when(actorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> actorService.getActorById(99L));
    }

    @Test
    void testCreateActor() {
        when(actorRepository.save(any(Actor.class))).thenReturn(actor);

        ActorDto result = actorService.createActor(actorDto);

        assertNotNull(result);
        assertEquals("JOHN", result.firstName());
        verify(actorRepository, times(1)).save(any(Actor.class));
    }

    @Test
    void testUpdateActor_Success() {
        when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
        when(actorRepository.save(any(Actor.class))).thenReturn(actor);

        ActorDto result = actorService.updateActor(1L, actorDto);

        assertNotNull(result);
        assertEquals("DOE", result.lastName());
    }



    @Test
    void testSearchActors() {
        when(actorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("JOHN", "JOHN"))
                .thenReturn(List.of(actor));

        List<ActorDto> result = actorService.searchActors("JOHN");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
