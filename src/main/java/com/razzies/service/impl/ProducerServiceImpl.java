package com.razzies.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.razzies.entity.MovieEntity;
import com.razzies.entity.ProducerEntity;
import com.razzies.model.ProducerGroup;
import com.razzies.model.ProducerWinner;
import com.razzies.model.RankingType;
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
	
	private Integer year = 0;
	private Integer countMoviesProducers = 0;

	@Override
	public void saveAll(final List<ProducerEntity> producers) {
		producerRepository.saveAll(producers);
	}
	
	@Override
	public ProducerEntity findByName(String name) {
		return producerRepository.findByName(name);
	}

	@Override
	public List<ProducerWinner> getProducersByRankingType(final RankingType rankingType) {
		final List<ProducerWinner> producersWinner = Lists.newArrayList();
		
		final List<ProducerGroup> producerGroups = producerRepository.findProducersGroup();
		final Map<String, Map<Integer, List<MovieEntity>>> mapProducerGroup = createMapProducerGroup(producerGroups);
		final Map<String, Integer> mapProducerCount = mapProducerCount(mapProducerGroup);
		
		final Entry<String, Integer> producerWinner = getProducerWinner(rankingType, mapProducerCount);
		
		if(producerWinner != null) {
			final Map<Integer, List<MovieEntity>> moviesEntities = mapProducerGroup.get(producerWinner.getKey());
			
			producersWinner.add(ProducerWinner.builder()
					.interval(producerWinner.getValue())
					.producer(producerWinner.getKey())
					.previousWin(getYearPreviousWin(moviesEntities.get(producerWinner.getValue())))
					.followingWin(getYearFollowingWin(moviesEntities.get(producerWinner.getValue())))
					.build());
		}
			
		return producersWinner;
	}
	
	private Entry<String, Integer> getProducerWinner(final RankingType rankingType, final Map<String, Integer> mapProducerCount){
		return RankingType.MAX.equals(rankingType) ? maxProducer(mapProducerCount): minProducer(mapProducerCount);
	}

	private Entry<String, Integer> maxProducer(Map<String, Integer> mapProducerCount) {
		return  mapProducerCount.entrySet().stream()
				.max((m1, m2) -> m1.getValue().compareTo(m2.getValue()))
				.orElse(null);
	}

	private Entry<String, Integer> minProducer(Map<String, Integer> mapProducerCount) {
		return  mapProducerCount.entrySet().stream()
				.min((m1, m2) -> m1.getValue().compareTo(m2.getValue()))
				.orElse(null);
	}
	
	private Integer getYearPreviousWin(final List<MovieEntity> movieEntities) {
		final MovieEntity movieEntity = movieEntities.stream()
			.min((m1, m2) -> m1.getYear().compareTo(m2.getYear()))
			.get();
		
		return movieEntity.getYear();
	}

	private Integer getYearFollowingWin(final List<MovieEntity> movieEntities) {
		final MovieEntity movieEntity = movieEntities.stream()
				.max((m1, m2) -> m1.getYear().compareTo(m2.getYear()))
				.get();
		
		return movieEntity.getYear();
	}
	
	private Map<String, Integer> mapProducerCount(final Map<String, Map<Integer, List<MovieEntity>>> map) {
		final Map<String, Integer> mapProducerCount = new HashMap<String, Integer>();
		
		map.entrySet().stream().forEach(m -> {
			String key = m.getKey();
			
			m.getValue().entrySet().stream().forEach(mm -> {
				mapProducerCount.put(key, mm.getKey());
			});
		});
		
		return mapProducerCount;
	}

	private Map<String, Map<Integer, List<MovieEntity>>> createMapProducerGroup(final List<ProducerGroup> producerGroups) {
		final Map<String, Map<Integer, List<MovieEntity>>> map = new HashMap<String, Map<Integer, List<MovieEntity>>>();
		
		producerGroups.stream()
			.filter(p -> p.getCount() > COUNT_WIN)
			.forEach(p -> {
				map.put(p.getName(), groupProducerWinners(p));
			});
		
		return map;
	}
	
	private Map<Integer, List<MovieEntity>> groupProducerWinners(ProducerGroup producerGroup) {
		final List<MovieEntity> movieEntities = movieRepository.findByProducersIdAndWinner(producerGroup.getId(), true);
		final Map<Integer, List<MovieEntity>> map = new HashMap<>();
		
		movieEntities.sort((m1, m2) -> m1.getYear().compareTo(m2.getYear()));
		
		setYear(0);
		
		movieEntities.forEach(m -> {
			if(!getYear().equals(0)) {
				setCountMoviesProducers(m.getYear() - year);
			}else {
				setYear(m.getYear());
			}
		});
		
		map.put(getCountMoviesProducers(), movieEntities);
		
		return map;
	}

	private Integer getYear() {
		return this.year;
	}
	
	private void setYear(Integer updateYear) {
		this.year = updateYear;
	}

	public Integer getCountMoviesProducers() {
		return countMoviesProducers;
	}

	public void setCountMoviesProducers(Integer countMoviesProducers) {
		this.countMoviesProducers = countMoviesProducers;
	}
}
