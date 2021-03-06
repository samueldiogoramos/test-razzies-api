package com.razzies;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.google.common.collect.Lists;
import com.razzies.entity.MovieEntity;
import com.razzies.entity.ProducerEntity;
import com.razzies.entity.StudioEntity;
import com.razzies.service.MovieService;
import com.razzies.service.ProducerService;
import com.razzies.service.StudioService;

@Component
public class LoadMovies implements ApplicationRunner {

	@Autowired
	private MovieService movieService;
	
	@Autowired
	private StudioService studioService;

	@Autowired
	private ProducerService producerService;
	
	@Value("${razzies.load.csv.file}")
	private String fileLoad;

	private static final Integer HEADER = 1;
	
	private static final Integer INDEX_YEAR = 0;
	private static final Integer INDEX_TITLE = 1;
	private static final Integer INDEX_STUDIOS = 2;
	private static final Integer INDEX_PRODUCERS = 3;
	private static final Integer INDEX_WINNER = 4;

	private static final String DELIMITTER_SEMICOLON = ";";
	private static final String DELIMITER_COMMA = ",";
	private static final String DELIMITER_AND = " and ";
	
	private static final String WINNER_YES = "yes";

	@Override
	public void run(ApplicationArguments args) throws Exception {

		final File file = ResourceUtils.getFile(String.format("classpath:static/%s", fileLoad));
		final List<MovieEntity> movieEntities = Lists.newArrayList();

		Files.lines(file.toPath())
			.skip(HEADER)
			.map(line -> line.split(DELIMITTER_SEMICOLON))
			.map(cols -> convertToMovie(cols))
			.forEach(movie -> {
				movieEntities.add(movie);
			});

		movieService.saveAll(movieEntities);
	}

	private MovieEntity convertToMovie(final String[] cols) {
		return MovieEntity.builder()
				.year(Integer.parseInt(cols[INDEX_YEAR]))
				.title(cols[INDEX_TITLE])
				.studios(convertToStudioList(cols[INDEX_STUDIOS]))
				.producers(convertToProducerList(cols[INDEX_PRODUCERS]))
				.winner(convertToWinner(cols))
				.build();
	}

	private List<StudioEntity> convertToStudioList(final String string) {
		final List<StudioEntity> studiosEntities = Lists.newArrayList();
		final List<StudioEntity> studiosEntitiesSave = Lists.newArrayList();
		
		Arrays.stream(string.split(DELIMITER_COMMA))
			.forEach(studio -> {
				final String studioName = StringUtils.trim(studio);
				
				StudioEntity studioEntity = studioService.findByName(studioName);
				
				if(studioEntity == null) {
					studioEntity = StudioEntity.builder()
							.name(studioName)
							.build();
					
					studiosEntitiesSave.add(studioEntity);
				}
				
				studiosEntities.add(studioEntity);
				
			});
		
		studioService.saveAll(studiosEntitiesSave);
		
		return studiosEntities;
	}
	
	private List<ProducerEntity> convertToProducerList(final String string) {
		final List<ProducerEntity> producers = Lists.newArrayList();
		
		Arrays.stream(string.split(DELIMITER_COMMA))
			.forEach(producer -> {
				producers.addAll(splitProducerName(producer));
			});
		
		return producers;
	}
	
	private List<ProducerEntity> splitProducerName(final String producer){
		List<ProducerEntity> producerEntities = Lists.newArrayList();
		List<ProducerEntity> producerEntitiesSave = Lists.newArrayList();
		
		final String [] names = producer.split(DELIMITER_AND);
		
		for(int i=0; i<names.length; i++) {
			String name = StringUtils.trim(names[i]);
			
			if(StringUtils.isNotBlank(name)) {
				ProducerEntity producerEntity = producerService.findByName(name);
				
				if(producerEntity == null) {
					producerEntity = ProducerEntity.builder()
							.name(name)
							.build();
					
					producerEntitiesSave.add(producerEntity);
				}
				
				producerEntities.add(producerEntity);
				
			}
		}
		
		producerService.saveAll(producerEntitiesSave);
		
		return producerEntities;
	}
	
	private boolean convertToWinner(final String[] strings) {
		boolean winner = false;
		
		if(strings.length == 5) {
			winner = WINNER_YES.equalsIgnoreCase(strings[INDEX_WINNER]);
		}
		
		return winner;
	}
	
}
