package com.schwartz.domain;

import lombok.Data;

@Data
public class BookDTO {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Boolean available;

}
