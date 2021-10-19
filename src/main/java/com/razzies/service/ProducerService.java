package com.razzies.service;

import java.util.List;

import com.razzies.entity.ProducerEntity;
import com.razzies.model.ProducerWinner;
import com.razzies.model.RankingType;

public interface ProducerService {
	void saveAll(List<ProducerEntity> producers);
	
	ProducerEntity findByName(String name);
	
	List<ProducerWinner> getProducersByRankingType(RankingType rankingType);
	
}
