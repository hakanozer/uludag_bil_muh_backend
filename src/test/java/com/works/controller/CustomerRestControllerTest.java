package com.works.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.works.CacheTestConfig;
import com.works.dto.CustomerRegisterRequestDto;
import com.works.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CacheTestConfig.class)
@WebMvcTest(CustomerRestController.class)
class CustomerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_Success() throws Exception {
        // Arrange
        CustomerRegisterRequestDto requestDto = new CustomerRegisterRequestDto();
        requestDto.setName("John");
        requestDto.setSurname("Doe");
        requestDto.setEmail("johndoe@example.com");
        requestDto.setPhone("+905555555555");
        requestDto.setEnabled(true);
        requestDto.setPassword("password123");

        Mockito.when(customerService.register(any(CustomerRegisterRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegister_InvalidData_EmailMissing() throws Exception {
        // Arrange
        CustomerRegisterRequestDto requestDto = new CustomerRegisterRequestDto();
        requestDto.setName("John");
        requestDto.setSurname("Doe");
        requestDto.setPhone("+905555555555");
        requestDto.setEnabled(true);
        requestDto.setPassword("password123");

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void testRegister_InvalidData_PhonePatternMismatch() throws Exception {
        // Arrange
        CustomerRegisterRequestDto requestDto = new CustomerRegisterRequestDto();
        requestDto.setName("John");
        requestDto.setSurname("Doe");
        requestDto.setEmail("johndoe@example.com");
        requestDto.setPhone("12345");
        requestDto.setEnabled(true);
        requestDto.setPassword("password123");

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.phone").exists());
    }

    @Test
    void testRegister_InvalidData_PasswordTooShort() throws Exception {
        // Arrange
        CustomerRegisterRequestDto requestDto = new CustomerRegisterRequestDto();
        requestDto.setName("John");
        requestDto.setSurname("Doe");
        requestDto.setEmail("johndoe@example.com");
        requestDto.setPhone("+905555555555");
        requestDto.setEnabled(true);
        requestDto.setPassword("123");

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    void testRegister_InvalidData_AllFieldsMissing() throws Exception {
        // Arrange
        CustomerRegisterRequestDto requestDto = new CustomerRegisterRequestDto();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/customer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.surname").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.phone").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }
}