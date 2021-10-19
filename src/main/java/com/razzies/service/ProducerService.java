package com.razzies.service;

import java.util.List;

import com.razzies.entity.ProducerEntity;
import com.razzies.model.ProducerWinner;

public interface ProducerService {
	void saveAll(List<ProducerEntity> producers);
	
	ProducerEntity findByName(String name);
	
	List<ProducerWinner> getProducerWithLongerRange2Awards();
	
	List<ProducerWinner> getProducerWhoGot2AwardsFaster();
	
}
