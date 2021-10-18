package com.razzies.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.razzies.entity.MovieEntity;
import com.razzies.entity.ProducerEntity;
import com.razzies.model.ProducerWinner;
import com.razzies.repository.MovieRepository;
import com.razzies.repository.ProducerRepository;
import com.razzies.service.ProducerService;

@Service
public class ProducerServiceImpl implements ProducerService{
	
	@Autowired
	private ProducerRepository producerRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	private static final Long COUNT_WIN = 1L;

	@Override
	public void saveAll(final List<ProducerEntity> producers) {
		producerRepository.saveAll(producers);
	}

	public ProducerWinner getProducerWithLongerRange2Awards() {
		final List<MovieEntity> movieEntities = movieRepository.findByWinner(true);
		ProducerWinner producerWinner = null;
		
		final List<Entry<String, List<ProducerWinner>>> producersWithMore2Wins = getProducersWithMore2Wins(movieEntities);
		
		final Map<Integer, String> map = calculateWinningPeriod(producersWithMore2Wins);
		
		final Entry<Integer, String> mapName = map.entrySet().stream()
				.max((m1, m2) -> m1.getKey().compareTo(m2.getKey()))
				.orElse(null);
		
		if(mapName != null) {
			producerWinner = ProducerWinner.builder().name(mapName.getValue()).build();
		}
		
		return producerWinner;
	}

	public ProducerWinner getProducerWhoGot2AwardsFaster() {
		final List<MovieEntity> movieEntities = movieRepository.findByWinner(true);
		ProducerWinner producerWinner = null;
		
		final List<Entry<String, List<ProducerWinner>>> producersWithMore2Wins = getProducersWithMore2Wins(movieEntities);
		
		Map<Integer, String> map = calculateWinningPeriod(producersWithMore2Wins);

		final Entry<Integer, String> mapName = map.entrySet().stream()
				.min((m1, m2) -> m1.getKey().compareTo(m2.getKey()))
				.orElse(null);
		
		if(mapName != null) {
			producerWinner = ProducerWinner.builder().name(mapName.getValue()).build();
		}

		return producerWinner;
	}
	
	private Map<Integer, String> calculateWinningPeriod(final List<Entry<String, List<ProducerWinner>>> producersWithMore2Wins) {
		final Map<Integer, String> map = new HashMap<Integer, String>();

		producersWithMore2Wins.stream().forEach(p -> {
			final List<Integer> years = Lists.newArrayList();

			for (ProducerWinner entry : p.getValue()) {
				years.add(entry.getYear());
			}

			Arrays.sort(years.toArray());

			//TODO Melhorar lógica para prever produtor com mais de 2 vitórias
			if (years.size() == 2) {
				map.put(years.get(1) - years.get(0), p.getKey());
			}
		});

		return map;
	}
	
	private List<Entry<String, List<ProducerWinner>>> getProducersWithMore2Wins(final List<MovieEntity> movieEntities) {
		final List<Entry<String, List<ProducerWinner>>> producersWithMore2Wins = Lists.newArrayList();
		
		final List<ProducerWinner> producerWinners = convertoToListProducerWinner(movieEntities);

		final Map<String, Long> producersPlusOneWin = filterPlusOneWin(producerWinners);

		final Map<String, List<ProducerWinner>> producersGroupWithMovies = groupProducersWithMovies(producerWinners);

		producersPlusOneWin.entrySet().stream().forEach(p -> {
			producersWithMore2Wins.add(producersGroupWithMovies.entrySet()
					.stream()
					.filter(pp -> pp.getKey().equals(p.getKey()))
					.findFirst()
					.get());
		});
		
		return producersWithMore2Wins;
	}
	
	private Map<String, List<ProducerWinner>> groupProducersWithMovies(final List<ProducerWinner> producerWinners) {
		return producerWinners.stream()
				.collect(Collectors.groupingBy(ProducerWinner::getName));
	}
	
	private Map<String, Long> filterPlusOneWin(final List<ProducerWinner> producerWinners) {
		return producerWinners.stream()
				.collect(Collectors.groupingBy(ProducerWinner::getName, Collectors.counting()))
				.entrySet()
				.stream()
				.filter(p -> p.getValue() > COUNT_WIN)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	private List<ProducerWinner> convertoToListProducerWinner (final List<MovieEntity> movieEntities) {
		List<ProducerWinner> producerWinners = Lists.newArrayList();
		
		movieEntities.forEach(m -> {
			m.getProducers().forEach(p -> {
				producerWinners.add(ProducerWinner.builder()
						.year(m.getYear())
						.name(p.getName())
						.build());
			});
		});
		
		producerWinners.sort((pw1, pw2) -> pw1.getName().compareTo(pw2.getName()));
		
		return producerWinners;
	}

}
