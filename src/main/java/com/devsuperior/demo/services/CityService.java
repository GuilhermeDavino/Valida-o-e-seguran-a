package com.devsuperior.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repositories.CityRepository;
import com.devsuperior.demo.repositories.EventRepository;
import com.devsuperior.demo.services.exceptions.DatabaseException;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CityService {
	@Autowired
	private CityRepository repository;
	
	@Autowired
	private EventRepository categoryRepository;

	@Transactional(readOnly = true)
	public List<CityDTO> findAllPaged(Sort sort) {
		List<City> products = repository.findAll(sort);
		return products.stream().map(item -> new CityDTO(item)).toList();
	}

	@Transactional(readOnly = true)
	public CityDTO findById(Long id) {
		City city = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));

		return new CityDTO(city);
	}

	@Transactional
	public CityDTO insert(CityDTO dto) {
		City entity = new City();
		entity = dtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new CityDTO(entity);
	}

	@Transactional
	public CityDTO update(Long id, CityDTO dto) {
		try {
			City entity = repository.getReferenceById(id);
			entity = dtoToEntity(dto, entity);
			repository.save(entity);
			return new CityDTO(entity);
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

	private City dtoToEntity(CityDTO dto, City entity) {
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.getEvents().clear();
		
		for(EventDTO eventDto : dto.getEvents()) {
			Event event = categoryRepository.findById(eventDto.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Recurso não encontrado"));
			entity.getEvents().add(event);
		}
		return entity;
	}
	
}
