package com.devsuperior.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;

import jakarta.validation.constraints.NotBlank;

public class CityDTO {
	
	private Long id;
	
	@NotBlank(message = "Campo requerido")
	private String name;
	private List<EventDTO> events = new ArrayList<>();
	
	public CityDTO() {
	}

	public CityDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public CityDTO(City entity) {
		id = entity.getId();
		name = entity.getName();
		for(Event event : entity.getEvents()) {
			events.add(new EventDTO(event));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EventDTO> getEvents() {
		return events;
	}
	
	
}
