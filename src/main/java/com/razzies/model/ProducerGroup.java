package com.razzies.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProducerGroup {
	private Long id;
	private String name;
	private Long count;
}
