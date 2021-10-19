package com.razzies.service;

import java.util.List;

import com.razzies.entity.StudioEntity;

public interface StudioService {
	void saveAll(List<StudioEntity> studios);
	
	StudioEntity findByName(String name);
}
