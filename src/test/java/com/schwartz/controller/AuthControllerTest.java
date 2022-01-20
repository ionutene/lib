package com.schwartz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schwartz.domain.LoginRequestDTO;
import com.schwartz.domain.LoginResponseDTO;
import com.schwartz.service.AuthService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;
    private MockMvc mockMvc;
    ObjectMapper mapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).alwaysDo(print()).build();
        mapper = new ObjectMapper();
    }

    private LoginRequestDTO getLoginRequest() {
        LoginRequestDTO l = new LoginRequestDTO();
        l.setEmail("email@gmail.com");
        l.setPassword("passwoed");
        return l;
    }

    private LoginResponseDTO getLoginResponse() {
        return new LoginResponseDTO("token", 1L, "email@gmail.com");
    }

    @Test
    void singINTest() throws Exception {
        Mockito.when(authService.login(any())).thenReturn(getLoginResponse());

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getLoginRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}