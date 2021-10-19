package com.razzies.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.razzies.entity.StudioEntity;

public interface StudioRepository extends JpaRepository<StudioEntity, Long>{

	StudioEntity findByName(final String name);
}
