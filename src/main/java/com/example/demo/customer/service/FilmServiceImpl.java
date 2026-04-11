package com.example.demo.customer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.customer.dto.FilmSearchDto;
import com.example.demo.customer.model.Film;
import com.example.demo.customer.repository.FilmRepository;

@Service
public class FilmServiceImpl implements FilmService {

    private FilmRepository filmRepository;

    // Constructor Injection (No Lombok)
    @Autowired
    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    // ================= SEARCH BY TITLE =================
    @Override
    public List<FilmSearchDto> searchFilmsByTitle(String title) {
        return filmRepository.searchByTitle(title)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ================= SEARCH BY ACTOR =================
    @Override
    public List<FilmSearchDto> searchFilmsByActor(String actor) {
        return filmRepository.searchByActor(actor)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ================= SEARCH BY CATEGORY =================
    @Override
    public List<FilmSearchDto> searchFilmsByCategory(String category) {
        return filmRepository.searchByCategory(category)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ================= MAPPER =================
    private FilmSearchDto mapToDto(Film f) {

        FilmSearchDto dto = new FilmSearchDto();

        dto.setFilmId(f.getFilmId());
        dto.setTitle(f.getTitle());
        dto.setDescription(f.getDescription());
        dto.setRating(f.getRating());
        dto.setRentalRate(f.getRentalRate());

        return dto;
    }
}
