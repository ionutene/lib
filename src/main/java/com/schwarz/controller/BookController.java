package com.schwarz.controller;

import com.schwarz.domain.BookDTO;
import com.schwarz.model.Book;
import com.schwarz.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
@Api("book")
@RequestMapping("/books")
@Slf4j
@AllArgsConstructor
public class BookController {
    private final BookService service;

    @ApiOperation("Create a new book.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created."),
            @ApiResponse(code = 400, message = "A new book cannot already have an ID book id exists.")})
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to save Book : {}", book);

        BookDTO result = service.save(book);

        return ResponseEntity
                .created(new URI("/book/books/" + result.getId()))
                .body(result);
    }

    @ApiOperation("Retrieve all books.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found."),
            @ApiResponse(code = 204, message = "No Books found.")
    }
    )
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        log.debug("REST request to get all Books");

        List<BookDTO> books = service.findAll();

        if (books.isEmpty()) {
            return ResponseEntity
                    .noContent()
                    .build();
        } else {
            return ResponseEntity
                    .ok()
                    .body(books);
        }
    }

    @ApiOperation("Retrieve a book by it`s ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found."),
            @ApiResponse(code = 404, message = "No Books found.")
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);

        Optional<BookDTO> book = service.findById(id);

        return book.map(value -> ResponseEntity
                .ok()
                .body(value)).orElseGet(() -> ResponseEntity
                .notFound()
                .build());
    }

    @ApiOperation("Update a book by it`s ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully found."),
            @ApiResponse(code = 404, message = "Book not found."),
            @ApiResponse(code = 400, message = "Bad request.")
    }
    )
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable(value = "id", required = false) final Long id, @RequestBody Book book) {
        log.debug("REST request to update Book : {}, {}", id, book);

        BookDTO result = service.update(id, book);

        return ResponseEntity
                .ok()
                .body(result);
    }

    @ApiOperation("Delete a book.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);

        service.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }


}
