package com.razzies.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProducerGroup {
	private Long id;
	private String name;
	private Long count;
}
