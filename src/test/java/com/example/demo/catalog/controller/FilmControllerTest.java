package com.example.demo.catalog.controller;

import com.example.demo.catalog.dto.FilmDto;
import com.example.demo.catalog.dto.FilmSummaryDto;
import com.example.demo.catalog.service.FilmService;
import com.example.demo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilmController.class)
@WithMockUser(authorities = "ROLE_ADMIN")
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    // Security mocks to satisfy SecurityConfig
    @MockBean
    private com.example.demo.auth.service.UserDetailsServiceImpl userDetailsService;
    
    @MockBean
    private com.example.demo.auth.service.JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private FilmSummaryDto filmSummaryDto;
    private FilmDto filmDto;

    @BeforeEach
    void setUp() {
        filmSummaryDto = new FilmSummaryDto(1L, "ACADEMY DINOSAUR", 2006, 
                new BigDecimal("0.99"), 86, "PG");
        
        filmDto = new FilmDto(1L, "ACADEMY DINOSAUR", "A Epic Drama", 2006, 
                1L, null, 6, new BigDecimal("0.99"), 86, 
                new BigDecimal("20.99"), "PG", "Deleted Scenes", 
                Collections.emptySet(), Collections.emptySet());
    }

    @Test
    void getAllFilms_ShouldReturnList() throws Exception {
        when(filmService.getAllFilms()).thenReturn(Arrays.asList(filmSummaryDto));

        mockMvc.perform(get("/api/v1/catalog/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("ACADEMY DINOSAUR"));
    }

    @Test
    void searchFilms_ShouldReturnFilteredList() throws Exception {
        when(filmService.searchFilms("ACADEMY", 2006)).thenReturn(Arrays.asList(filmSummaryDto));

        mockMvc.perform(get("/api/v1/catalog/films/search")
                        .param("title", "ACADEMY")
                        .param("year", "2006"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("ACADEMY DINOSAUR"));
    }

    @Test
    void getFilmsByCategory_ShouldReturnList() throws Exception {
        when(filmService.getFilmsByCategory("Action")).thenReturn(Arrays.asList(filmSummaryDto));

        mockMvc.perform(get("/api/v1/catalog/films/category/Action"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("ACADEMY DINOSAUR"));
    }

    @Test
    void getFilmsByActor_ShouldReturnList() throws Exception {
        when(filmService.getFilmsByActor(1L)).thenReturn(Arrays.asList(filmSummaryDto));

        mockMvc.perform(get("/api/v1/catalog/films/actor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("ACADEMY DINOSAUR"));
    }

    @Test
    void getFilmsByActorName_ShouldReturnList() throws Exception {
        when(filmService.getFilmsByActorName("PENELOPE")).thenReturn(Arrays.asList(filmSummaryDto));

        mockMvc.perform(get("/api/v1/catalog/films/actor")
                        .param("name", "PENELOPE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("ACADEMY DINOSAUR"));
    }

    @Test
    void getFilmsByLanguage_ShouldReturnList() throws Exception {
        when(filmService.getFilmsByLanguage("English")).thenReturn(Arrays.asList(filmSummaryDto));

        mockMvc.perform(get("/api/v1/catalog/films/language/English"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("ACADEMY DINOSAUR"));
    }

    @Test
    void getFilmsByRating_ShouldReturnList() throws Exception {
        when(filmService.getFilmsByRating("PG")).thenReturn(Arrays.asList(filmSummaryDto));

        mockMvc.perform(get("/api/v1/catalog/films/rating/PG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("ACADEMY DINOSAUR"));
    }

    @Test
    void getFilmById_WhenExists_ShouldReturnFilm() throws Exception {
        when(filmService.getFilmById(1L)).thenReturn(filmDto);

        mockMvc.perform(get("/api/v1/catalog/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("ACADEMY DINOSAUR"));
    }

    @Test
    void getFilmById_WhenNotExists_ShouldReturn404() throws Exception {
        when(filmService.getFilmById(1L)).thenThrow(new ResourceNotFoundException("Film not found"));

        mockMvc.perform(get("/api/v1/catalog/films/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchFilm_ShouldReturnUpdatedFilm() throws Exception {
        com.example.demo.catalog.dto.FilmUpdateDto updates = new com.example.demo.catalog.dto.FilmUpdateDto();
        updates.setTitle("UPDATED TITLE");

        when(filmService.patchFilm(eq(1L), any(com.example.demo.catalog.dto.FilmUpdateDto.class))).thenReturn(filmDto);

        mockMvc.perform(patch("/api/v1/catalog/films/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk());
    }
}
