package com.schwartz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schwartz.domain.BookDTO;
import com.schwartz.model.Book;
import com.schwartz.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class BookControllerTest {
    private static final String RESOURCE = "/books/";
    @InjectMocks
    private BookController bookController;
    @Mock
    BookService bookService;
    private MockMvc mockMvc;
    ObjectMapper mapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).alwaysDo(print()).build();
        mapper = new ObjectMapper();
    }

    private Book getBook() {
        Book b = new Book();
        b.setId(1L);
        b.setTitle("Clean Code");
        b.setAuthor("Robert C. Martin");
        b.setPublisher("Prentice Hall");
        b.setIsbn("9780132350884");
        b.setAvailable(true);
        b.setUserID(0L);
        b.setTransactionID(0L);
        b.setAvailable(true);
        return b;
    }

    private BookDTO getBookDTO() {
        BookDTO b = new BookDTO();
        b.setId(1L);
        b.setTitle("Clean Code");
        b.setAuthor("Robert C. Martin");
        b.setPublisher("Prentice Hall");
        b.setIsbn("9780132350884");
        b.setAvailable(true);
        return b;
    }

    private List<BookDTO> getBooksDTO() {
        return Collections.singletonList(getBookDTO());
    }

    @Test
    @WithMockUser(username = "USER")
    void getAllBooksTest() throws Exception {
        List<BookDTO> books = getBooksDTO();
        Mockito.when(bookService.findAll()).thenReturn(books);

        ResultActions resultActions = mockMvc.perform(get("/books")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "USER")
    void getAllBooks_noneFoundTest() throws Exception {
        Mockito.when(bookService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(RESOURCE)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "USER")
    void getABookTest() throws Exception {
        Mockito.when(bookService.findById(any()))
                .thenReturn(java.util.Optional.of(getBookDTO()));

        mockMvc.perform(get(RESOURCE + 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "USER")
    void getABook_noFoundTest() throws Exception {
        Mockito.when(bookService.findById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get(RESOURCE + 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    @WithMockUser(username = "USER")
    void putABookTest() throws Exception {
        Mockito.when(bookService.save(any()))
                .thenReturn(getBookDTO());

        mockMvc.perform(MockMvcRequestBuilders.post(RESOURCE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(getBook()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "USER")
    void updateABookTest() throws Exception {
        Book updatedBook = getBook();
        updatedBook.setAvailable(true);
        updatedBook.setTransactionID(123L);
        updatedBook.setUserID(9L);

        BookDTO updatedBookDTO = getBookDTO();
        updatedBookDTO.setAvailable(true);

        Mockito.when(bookService.save(updatedBook))
                .thenReturn(updatedBookDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(RESOURCE + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedBook))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "USER")
    void updateABook_missingTest() throws Exception {
        Book updatedBook = getBook();
        updatedBook.setAvailable(true);
        updatedBook.setTransactionID(123L);
        updatedBook.setUserID(9L);

        BookDTO updatedBookDTO = getBookDTO();
        updatedBookDTO.setAvailable(true);

        Mockito.when(bookService.save(updatedBook))
                .thenReturn(updatedBookDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(RESOURCE + 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedBook))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "USER")
    void updateABook_withDifferentIDTest() throws Exception {
        Book updatedBook = getBook();
        updatedBook.setId(2L);
        updatedBook.setAvailable(true);
        updatedBook.setTransactionID(123L);
        updatedBook.setUserID(9L);

        BookDTO updatedBookDTO = getBookDTO();
        updatedBookDTO.setAvailable(true);

        Mockito.when(bookService.save(updatedBook))
                .thenReturn(updatedBookDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(RESOURCE + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedBook))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @WithMockUser(username = "USER")
    void deleteABookTest() throws Exception {
        mockMvc.perform(delete(RESOURCE + 1L))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteById(1L);
    }

}
