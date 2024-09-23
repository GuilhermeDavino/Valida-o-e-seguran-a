package com.devsuperior.demo.dto;

import java.util.HashSet;
import java.util.Set;

import com.devsuperior.demo.entities.Role;
import com.devsuperior.demo.entities.User;

public class UserDTO {
	private Long id;
	private String email;
	private Set<RoleDTO> roles = new HashSet<>();
	
	public UserDTO() {
		
	}

	public UserDTO(User entity) {
		id = entity.getId();
		email = entity.getEmail();
		for(Role role : entity.getRoles()) {
			roles.add(new RoleDTO(role));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}
	
	
	
	
	
}
