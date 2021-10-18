package com.razzies.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.razzies.entity.MovieEntity;

public interface MovieRepository extends JpaRepository<MovieEntity, Long>{

	List<MovieEntity> findByWinner(boolean winner);

}
