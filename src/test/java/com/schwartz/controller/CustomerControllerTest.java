package com.schwartz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schwartz.domain.CustomerRequestDTO;
import com.schwartz.domain.CustomerResponseDTO;
import com.schwartz.exceptions.BusinessException;
import com.schwartz.repository.CustomerRepository;
import com.schwartz.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class CustomerControllerTest {
    private static final String RESOURCE = "/customers/";
    @InjectMocks
    CustomerController customerController;
    @Mock
    CustomerService service;
    @Mock
    CustomerRepository customerRepository;
    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).alwaysDo(print()).build();
        mapper = new ObjectMapper();
    }

    private CustomerRequestDTO getCustomer() {
        CustomerRequestDTO c = new CustomerRequestDTO();
        c.setId(1L);
        c.setEmail("email@gmail.com");
        c.setPassword("passwoed");
        return c;
    }

    private CustomerResponseDTO getCustomerDTO() {
        CustomerResponseDTO c = new CustomerResponseDTO();
        c.setId(1L);
        c.setEmail("email@gmail.com");
        return c;
    }

    @Test
    void singUPTest() throws Exception {
        Mockito.when(service.create(getCustomer()))
                .thenReturn(getCustomerDTO());

        mockMvc.perform(MockMvcRequestBuilders.post(RESOURCE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCustomer()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    void singUP_withInvalidEMailTest() {
        CustomerRequestDTO customerRequestDTO = getCustomer();
        customerRequestDTO.setEmail("invalid");
        Mockito.when(service.create(customerRequestDTO))
                .thenThrow(BusinessException.class);
    }

    @Test
    void singUP_withExistingTest() {
        Mockito.when(customerRepository.existsByEmail("email@gmail.com"))
                .thenReturn(true);

        Mockito.when(service.create(getCustomer()))
                .thenThrow(BusinessException.class);

    }
}