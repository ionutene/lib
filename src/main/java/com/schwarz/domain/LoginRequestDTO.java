package com.schwarz.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class LoginRequestDTO {
	@NonNull
	private String email;
	@NonNull
	private String password;
}
