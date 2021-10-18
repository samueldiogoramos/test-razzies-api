package com.razzies.service;

import java.util.List;

import com.razzies.entity.ProducerEntity;
import com.razzies.model.ProducerWinner;

public interface ProducerService {
	void saveAll(List<ProducerEntity> producers);
	
	ProducerWinner getProducerWithLongerRange2Awards();
	
	ProducerWinner getProducerWhoGot2AwardsFaster();
	
}
