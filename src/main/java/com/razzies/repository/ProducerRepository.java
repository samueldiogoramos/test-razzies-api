package com.razzies.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.razzies.entity.ProducerEntity;
import com.razzies.model.ProducerGroup;

public interface ProducerRepository extends JpaRepository<ProducerEntity, Long>{

	ProducerEntity findByName(String name);
	
	@Query("select new com.razzies.model.ProducerGroup(p.id, p.name, count(p)) "
		 + "from MovieEntity m "
		 + "inner join m.producers p "
		 + "where m.winner is true "
		 + "group by p.id, p.name ")
	List<ProducerGroup> findProducersGroup();

}
