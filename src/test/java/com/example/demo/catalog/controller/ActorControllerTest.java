package com.example.demo.catalog.controller;

import com.example.demo.catalog.dto.ActorDto;
import com.example.demo.catalog.dto.ActorUpdateDto;
import com.example.demo.catalog.service.ActorService;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ActorController.class)
@WithMockUser(authorities = "ROLE_ADMIN")
public class ActorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActorService actorService;

    // Security mocks to satisfy SecurityConfig
    @MockBean
    private com.example.demo.auth.service.UserDetailsServiceImpl userDetailsService;
        
    @MockBean
    private com.example.demo.auth.service.JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private ActorDto actorDto;

    @BeforeEach
    void setUp() {
        actorDto = new ActorDto(1L, "PENELOPE", "GUINESS");
    }

    @Test
    void getAllActors_ShouldReturnList() throws Exception {
        List<ActorDto> actors = Arrays.asList(actorDto);
        when(actorService.getAllActors()).thenReturn(actors);

        mockMvc.perform(get("/api/v1/catalog/actors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("PENELOPE"));
    }

    @Test
    void searchActors_ShouldReturnFilteredList() throws Exception {
        List<ActorDto> actors = Arrays.asList(actorDto);
        when(actorService.searchActors("PENELOPE")).thenReturn(actors);

        mockMvc.perform(get("/api/v1/catalog/actors/search")
                        .param("name", "PENELOPE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("PENELOPE"));
    }



    @Test
    void createActor_ShouldReturnCreated() throws Exception {
        when(actorService.createActor(any(ActorDto.class))).thenReturn(actorDto);

        mockMvc.perform(post("/api/v1/catalog/actors")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("PENELOPE"));
    }

    @Test
    void updateActor_ShouldReturnUpdatedActor() throws Exception {
        when(actorService.updateActor(eq(1L), any(ActorDto.class))).thenReturn(actorDto);

        mockMvc.perform(put("/api/v1/catalog/actors/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("PENELOPE"));
    }



    @Test
    void patchActor_ShouldReturnUpdatedActor() throws Exception {
        ActorUpdateDto updates = new ActorUpdateDto();
        updates.setFirstName("NEW NAME");

        when(actorService.patchActor(eq(1L), any(ActorUpdateDto.class))).thenReturn(actorDto);

        mockMvc.perform(patch("/api/v1/catalog/actors/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk());
    }
}
