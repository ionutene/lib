package com.schwartz.domain;

import lombok.Data;

@Data
public class CustomerRequestDTO {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String password;

}
