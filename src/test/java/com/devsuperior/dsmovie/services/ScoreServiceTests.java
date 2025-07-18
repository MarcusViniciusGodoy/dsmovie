package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ScoreRepository scoreRepository;

	private Long invalidMovieId;
	private ScoreDTO scoreDTO;
	private UserEntity user;
	private MovieEntity movie;
	private ScoreEntity score;

	@BeforeEach
	void setUp() throws Exception{
		invalidMovieId = 100L;

		score = ScoreFactory.createScoreEntity();
		movie = score.getId().getMovie();
		user = score.getId().getUser();
		scoreDTO = new ScoreDTO(movie.getId(), score.getValue());

		Mockito.when(userService.authenticated()).thenReturn(user);
		Mockito.when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
		Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(score);
		Mockito.when(movieRepository.save(any())).thenReturn(movie);

		Mockito.when(userService.authenticated()).thenReturn(user);
		Mockito.when(movieRepository.findById(invalidMovieId)).thenReturn(Optional.empty());
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {

		MovieDTO result = service.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(movie.getId(), result.getId());
		Assertions.assertEquals(movie.getTitle(), result.getTitle());
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

		ScoreDTO invalidScoreDTO = new ScoreDTO(invalidMovieId, 5.0);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.saveScore(invalidScoreDTO);
		});
	}
}
