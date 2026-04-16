package com.example.demo.customer.controller;

import com.example.demo.customer.dto.FilmSearchDto;
import com.example.demo.customer.service.FilmService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilmController.class)
@Import(FilmControllerTest.TestSecurityConfig.class)
public class FilmControllerTest {

    @MockBean
    private FilmService filmService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================================================================
    // 1. UNIT TESTS
    // =========================================================================
    @Nested
    @ExtendWith(MockitoExtension.class)
    class UnitTests {

        @Mock
        private FilmService unitFilmService;

        private FilmController unitFilmController;

        private FilmSearchDto searchDto;

        @BeforeEach
        void setUp() {
            unitFilmController = new FilmController(unitFilmService);

            searchDto = new FilmSearchDto();
            searchDto.setTitle("Academy Dinosaur");
        }

        @Test
        void testSearchByTitle() {
            when(unitFilmService.searchFilmsByTitle("Academy")).thenReturn(Arrays.asList(searchDto));
            List<FilmSearchDto> result = unitFilmController.searchByTitle("Academy");
            assertEquals(1, result.size());
            assertEquals("Academy Dinosaur", result.get(0).getTitle());
            verify(unitFilmService).searchFilmsByTitle("Academy");
        }

        @Test
        void testSearchByActor() {
            when(unitFilmService.searchFilmsByActor("PENELOPE GUINESS")).thenReturn(Arrays.asList(searchDto));
            List<FilmSearchDto> result = unitFilmController.searchByActor("PENELOPE GUINESS");
            assertEquals(1, result.size());
            verify(unitFilmService).searchFilmsByActor("PENELOPE GUINESS");
        }

        @Test
        void testSearchByCategory() {
            when(unitFilmService.searchFilmsByCategory("Action")).thenReturn(Arrays.asList(searchDto));
            List<FilmSearchDto> result = unitFilmController.searchByCategory("Action");
            assertEquals(1, result.size());
            verify(unitFilmService).searchFilmsByCategory("Action");
        }
    }

    // =========================================================================
    // 2. API TESTS — inherits outer @WebMvcTest context
    // =========================================================================
    @Nested
    class ApiTests {

        private FilmSearchDto searchDto;

        @BeforeEach
        void setUp() {
            searchDto = new FilmSearchDto();
            searchDto.setTitle("Academy Dinosaur");
        }

        @Test
        void GET_searchByTitle_returns200() throws Exception {
            when(filmService.searchFilmsByTitle("Academy")).thenReturn(Arrays.asList(searchDto));
            mockMvc.perform(get("/api/v1/films/search/title").param("title", "Academy"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value("Academy Dinosaur"));
        }

        @Test
        void GET_searchByActor_returns200() throws Exception {
            when(filmService.searchFilmsByActor("PENELOPE GUINESS")).thenReturn(Arrays.asList(searchDto));
            mockMvc.perform(get("/api/v1/films/search/actor").param("actor", "PENELOPE GUINESS"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].title").value("Academy Dinosaur"));
        }

        @Test
        void GET_searchByCategory_returns200() throws Exception {
            when(filmService.searchFilmsByCategory("Action")).thenReturn(Arrays.asList(searchDto));
            mockMvc.perform(get("/api/v1/films/search/category").param("category", "Action"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }
    }

    static class TestSecurityConfig {
        @org.springframework.boot.test.mock.mockito.MockBean
        private com.example.demo.auth.service.JwtService jwtService;

        @org.springframework.boot.test.mock.mockito.MockBean
        private com.example.demo.auth.service.UserDetailsServiceImpl userDetailsService;
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
