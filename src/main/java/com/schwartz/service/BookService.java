package com.schwartz.service;

import com.schwartz.domain.BookDTO;
import com.schwartz.exceptions.BusinessException;
import com.schwartz.exceptions.NotFoundException;
import com.schwartz.model.Book;
import com.schwartz.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ModelMapper mapper;

    public BookDTO save(Book book) {

        if (book.getId() != null) {
            throw new BusinessException("A new book cannot already have an ID book id exists");
        }

        return mapper.map(bookRepository.save(book), BookDTO.class);
    }

    public BookDTO update(Long id, Book book) {

        if (book.getId() == null) {
            throw new BusinessException("Invalid id book id null");
        }
        if (!Objects.equals(id, book.getId())) {
            throw new BusinessException("Invalid ID book id invalid");
        }

        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Entity not found book id not found");
        }
        return mapper.map(bookRepository.save(book), BookDTO.class);

    }

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(e -> mapper.map(e, BookDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<BookDTO> findById(Long id) {
        Optional<Book> book = bookRepository.findById(id);

        return book.map(value -> mapper.map(value, BookDTO.class));
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
