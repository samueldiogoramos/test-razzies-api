package com.razzies.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.razzies.entity.MovieEntity;

public interface MovieRepository extends JpaRepository<MovieEntity, Long>{

	@Deprecated
	List<MovieEntity> findByWinner(boolean winner);
	
	List<MovieEntity> findByProducersIdAndWinner(Long id, boolean winner);

}
