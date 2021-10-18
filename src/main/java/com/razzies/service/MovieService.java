package com.razzies.service;

import java.util.List;

import com.razzies.entity.MovieEntity;

public interface MovieService {
	void saveAll(List<MovieEntity> movies);
	
}
