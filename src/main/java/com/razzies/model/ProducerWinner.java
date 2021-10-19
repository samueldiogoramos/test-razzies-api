package com.razzies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProducerWinner {
	@JsonIgnore
	private Integer year;
	
	private String producer;
	private Integer interval;
	private Integer previousWin;
	private Integer followingWin;
}
