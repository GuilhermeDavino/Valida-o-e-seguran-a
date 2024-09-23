package com.devsuperior.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repositories.CityRepository;
import com.devsuperior.demo.repositories.EventRepository;
import com.devsuperior.demo.services.exceptions.DatabaseException;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService {
	
	@Autowired
	private EventRepository repository;
	
	@Autowired
	private CityRepository cityRepository;

	@Transactional(readOnly = true)
	public Page<EventDTO> findAllPaged(Pageable pageable) {
		Page<Event> categories = repository.findAll(pageable);
		return categories.map(item -> new EventDTO(item));
	}

	@Transactional(readOnly = true)
	public EventDTO findById(Long id) {
		Event category = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));

		return new EventDTO(category);
	}

	@Transactional
	public EventDTO insert(EventDTO dto) {
		Event entity = new Event(dto);
		City city = cityRepository.getReferenceById(dto.getCityId());
		entity.setCity(city);
		entity = repository.save(entity);
		return new EventDTO(entity);
	}

	@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event entity = repository.getReferenceById(id);
			entity = dtoToEntity(entity, dto);
			repository.save(entity);
			return new EventDTO(entity);
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id inválido, Recurso não encontrado");
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		
		if(!repository.existsById(id)) {
			throw new ResourceNotFoundException("Este recurso não existe");
		}
		
		try {
			
			repository.deleteById(id);
		
		} catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
		
	}

	public Event dtoToEntity(Event entity, EventDTO dto) {
		entity.setName(dto.getName());
		return entity;
	}
}
