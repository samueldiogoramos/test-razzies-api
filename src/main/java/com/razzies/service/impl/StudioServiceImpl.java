package com.razzies.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razzies.entity.StudioEntity;
import com.razzies.repository.StudioRepository;
import com.razzies.service.StudioService;

@Service
public class StudioServiceImpl implements StudioService{
	
	@Autowired
	private StudioRepository studioRepository;

	@Override
	public void saveAll(final List<StudioEntity> studios) {
		studioRepository.saveAll(studios);
	}

	@Override
	public StudioEntity findByName(String name) {
		return studioRepository.findByName(name);
	}

}
