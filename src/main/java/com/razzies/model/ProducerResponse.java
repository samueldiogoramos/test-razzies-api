package com.razzies.model;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProducerResponse implements Serializable {
	private static final long serialVersionUID = 7818393828764708120L;

	private List<ProducerWinner> min;
	private List<ProducerWinner> max;
}
