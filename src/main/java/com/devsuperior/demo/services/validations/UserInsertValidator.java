package com.devsuperior.demo.services.validations;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.demo.controllers.exceptions.FieldMessage;
import com.devsuperior.demo.dto.UserInsertDTO;
import com.devsuperior.demo.entities.User;
import com.devsuperior.demo.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Override
	public void initialize(UserInsertValid ann) {
	}
	
	@Autowired
	private UserRepository repository;

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		User user = repository.findByEmail(dto.getEmail());
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		
		if(user != null) {
			list.add(new FieldMessage("email", "This email already exists"));
		}
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}