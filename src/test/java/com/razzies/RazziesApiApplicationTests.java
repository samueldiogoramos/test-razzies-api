package com.razzies;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

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

}
