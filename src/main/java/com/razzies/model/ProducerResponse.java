package com.razzies.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProducerResponse implements Serializable {
	private static final long serialVersionUID = 7818393828764708120L;

	private ProducerWinner producerWithLongerRange2Awards;
	private ProducerWinner producerWhoGot2AwardsFaster;
}
