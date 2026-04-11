package com.example.demo.customer.service;

import com.example.demo.customer.dto.*;
import java.util.*;

public interface FilmService {
	List<FilmSearchDto> searchFilmsByTitle(String title);

    List<FilmSearchDto> searchFilmsByActor(String actorName);

    List<FilmSearchDto> searchFilmsByCategory(String category);
}
