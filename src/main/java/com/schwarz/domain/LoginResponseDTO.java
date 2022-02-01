package com.schwarz.domain;

import lombok.Data;

@Data
public class LoginResponseDTO {
	private String token;
	private Long id;
	private String email;


	public LoginResponseDTO(String accessToken, Long id, String email) {
		this.token = accessToken;
		this.id = id;
		this.email = email;
	}

}
