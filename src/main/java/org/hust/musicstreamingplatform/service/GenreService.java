package org.hust.musicstreamingplatform.service;

import lombok.RequiredArgsConstructor;
import org.hust.musicstreamingplatform.exception.genre.GenreNotFoundException;
import org.hust.musicstreamingplatform.model.Genre;
import org.hust.musicstreamingplatform.repository.GenreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;




}
