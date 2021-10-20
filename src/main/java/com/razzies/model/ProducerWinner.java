package com.razzies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProducerWinner {
	@JsonIgnore
	private Integer year;
	
	private String producer;
	private Integer interval;
	private Integer previousWin;
	private Integer followingWin;
}
