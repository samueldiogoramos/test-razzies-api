package com.razzies.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "year", nullable = false)
	private Integer year;

	@Column(name = "title", nullable = false)
	private String title;

	@OneToMany
	private List<StudioEntity> studios;

	@OneToMany
	private List<ProducerEntity> producers;

	@Column(name = "winner", nullable = false)
	private boolean winner;
}
