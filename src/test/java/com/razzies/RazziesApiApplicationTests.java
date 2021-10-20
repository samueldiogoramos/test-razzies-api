package com.razzies;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;

import com.razzies.controller.ProducerController;
import com.razzies.model.ProducerResponse;
import com.razzies.model.ProducerWinner;
import com.razzies.repository.MovieRepository;
import com.razzies.repository.ProducerRepository;
import com.razzies.repository.StudioRepository;

@SpringBootTest
class RazziesApiApplicationTests {
	
	private static final Integer HEADER = 1;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private ProducerRepository producerRepository;
	
	@Autowired
	private StudioRepository studioRepository;
	
	@Autowired
	private ProducerController producerController;
	
	@Value("${razzies.load.csv.file}")
	private String fileLoad;
	
	@Test
	void loadCSV_movies_ok() throws IOException {
		final File file = ResourceUtils.getFile(String.format("classpath:static/%s", fileLoad));
		final long countRepository = movieRepository.count();
		final long countReg = Files.lines(file.toPath())
				.skip(HEADER)
				.count();
		
		assertEquals(countRepository, countReg);
	}
	
	@Test
	void loadCSV_studio_exists() {
		final long countStudios = studioRepository.count();
		
		assertEquals(true, countStudios != 0);
	}
	
	@Test
	void loadCSV_producers_exists() {
		final long countProducers = producerRepository.count();
		
		assertEquals(true, countProducers != 0);
	}
	
	@Test
	void test_HttpStatusOK() {
		final ResponseEntity<ProducerResponse> response = producerController.obterProducers();
	
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void test_ReturnMax_OK() {
		final ResponseEntity<ProducerResponse> response = producerController.obterProducers();
		
		ProducerResponse producerResponse = response.getBody();
		
		Assertions.assertTrue(producerResponse.getMax().stream().findFirst().isPresent());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void test_ReturnMin_OK() {
		final ResponseEntity<ProducerResponse> response = producerController.obterProducers();
		
		ProducerResponse producerResponse = response.getBody();
		
		Assertions.assertTrue(producerResponse.getMin().stream().findFirst().isPresent());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void test_ReturnProducer_OK() {
		final ResponseEntity<ProducerResponse> response = producerController.obterProducers();
		
		ProducerWinner producerResponse = response.getBody().getMax().stream().findFirst().get();
		
		assertEquals(producerResponse.getFollowingWin(), 2015);
		assertEquals(producerResponse.getInterval(), 13);
		assertEquals(producerResponse.getPreviousWin(), 2002);
		assertEquals(producerResponse.getProducer(), "Matthew Vaughn");
	}

}
