package com.razzies.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razzies.entity.MovieEntity;
import com.razzies.repository.MovieRepository;
import com.razzies.service.MovieService;

@Service
public class MovieServiceImpl implements MovieService{
	
	@Autowired
	private MovieRepository movieRepository;

	@Override
	public void saveAll(final List<MovieEntity> movies) {
		movieRepository.saveAll(movies);
	}

}
