package com.example.demo.catalog.controller;

import com.example.demo.catalog.dto.CategoryDto;
import com.example.demo.catalog.service.CategoryService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@WithMockUser(authorities = "ROLE_ADMIN")
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    // Security mocks to satisfy SecurityConfig
    @MockBean
    private com.example.demo.auth.service.UserDetailsServiceImpl userDetailsService;
    
    @MockBean
    private com.example.demo.auth.service.JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        categoryDto = new CategoryDto(1L, "Action");
    }

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        List<CategoryDto> categories = Arrays.asList(categoryDto);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/catalog/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Action"));
    }

    @Test
    void searchCategories_ShouldReturnFilteredList() throws Exception {
        List<CategoryDto> categories = Arrays.asList(categoryDto);
        when(categoryService.searchCategories("Action")).thenReturn(categories);

        mockMvc.perform(get("/api/v1/catalog/categories/search")
                        .param("name", "Action"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Action"));
    }

    @Test
    void getCategoryById_WhenExists_ShouldReturnCategory() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(categoryDto);

        mockMvc.perform(get("/api/v1/catalog/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Action"));
    }

    @Test
    void getCategoryById_WhenNotExists_ShouldReturn404() throws Exception {
        when(categoryService.getCategoryById(1L)).thenThrow(new ResourceNotFoundException("Category not found"));

        mockMvc.perform(get("/api/v1/catalog/categories/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCategory_ShouldReturnCreated() throws Exception {
        when(categoryService.createCategory(any(CategoryDto.class))).thenReturn(categoryDto);

        mockMvc.perform(post("/api/v1/catalog/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Action"));
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory() throws Exception {
        when(categoryService.updateCategory(eq(1L), any(CategoryDto.class))).thenReturn(categoryDto);

        mockMvc.perform(put("/api/v1/catalog/categories/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Action"));
    }

    @Test
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/v1/catalog/categories/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void patchCategory_ShouldReturnUpdatedCategory() throws Exception {
        com.example.demo.catalog.dto.CategoryUpdateDto updates = new com.example.demo.catalog.dto.CategoryUpdateDto();
        updates.setName("NEW NAME");

        when(categoryService.patchCategory(eq(1L), any(com.example.demo.catalog.dto.CategoryUpdateDto.class))).thenReturn(categoryDto);

        mockMvc.perform(patch("/api/v1/catalog/categories/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk());
    }
}
