package com.enigma.tokonyadia.controller;

import com.enigma.tokonyadia.entity.Customer;
import com.enigma.tokonyadia.entity.UserDetailsImpl;
import com.enigma.tokonyadia.model.response.CommonResponse;
import com.enigma.tokonyadia.security.UserSecurity;
import com.enigma.tokonyadia.service.CustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @MockBean
    private CustomerService customerService;
    @MockBean
    private UserSecurity userSecurity;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "edy", roles = "ADMIN")
    @Test
    void itShouldHave200StatusAndValidResponseWhenGetAll() throws Exception {
        List<Customer> customers = List.of(
                Customer.builder()
                        .name("edy")
                        .email("edy@gmail.com")
                        .mobilePhone("08124")
                        .build(),
                Customer.builder()
                        .name("fauzan")
                        .email("fauzan@gmail.com")
                        .mobilePhone("081245")
                        .build()
        );
        when(customerService.searchByNameOrPhoneOrEmail(anyString(), anyString(), anyString()))
                .thenReturn(customers);

        CommonResponse<List<Customer>> response = CommonResponse.<List<Customer>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all customer")
                .data(customers)
                .build();
        String[] arr = {};

        mockMvc.perform(get("/api/v1/customers")
                        .param("name", "any")
                        .param("mobilePhone", "any")
                        .param("email", "any")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<List<Customer>> commonResponse = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<CommonResponse<List<Customer>>>() {});
                    Assertions.assertEquals(response.getMessage(), commonResponse.getMessage());
                    Assertions.assertEquals(response.getData(), commonResponse.getData());
                });
    }

    @WithMockUser(username = "edy")
    @Test
    void itShouldHave200StatusCodeAndValidResponseWhenGetById() throws Exception {
        String id = "edy-1";
        Customer customer = Customer.builder()
                .id(id)
                .name("edy")
                .email("edy@gmail.com")
                .mobilePhone("08123")
                .build();
        when(customerService.getById(anyString())).thenReturn(customer);

        CommonResponse<Customer> response = CommonResponse.<Customer>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get customer by id")
                .data(customer)
                .build();

        mockMvc.perform(get("/api/v1/customers/" + id).contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<Customer> actual = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    Assertions.assertEquals(response.getStatusCode(), actual.getStatusCode());
                    Assertions.assertEquals(response.getData(), actual.getData());
                });
    }

    @WithMockUser(username = "edy", roles = "CUSTOMER")
    @Test
    void itShouldHave200StatusAndValidResponseWhenUpdateCustomer() throws Exception {
        Customer customer = Customer.builder()
                .id("edy-1")
                .name("edy")
                .email("edy@gmail.com")
                .mobilePhone("08123")
                .build();
        String customerJson = objectMapper.writeValueAsString(customer);

        when(userSecurity.checkCustomer(isA(Authentication.class), isA(String.class))).thenReturn(true);
        when(customerService.update(any())).thenReturn(customer);
        CommonResponse<Customer> response = CommonResponse.<Customer>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update customer")
                .data(customer)
                .build();

        mockMvc.perform(put("/api/v1/customers")
                        .contentType("application/json")
                        .content(customerJson)
                ).andExpect(status().isOk())
                .andDo(result -> {
                    CommonResponse<Customer> actual = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    Assertions.assertEquals(response.getStatusCode(), actual.getStatusCode());
                    Assertions.assertEquals(response.getData(), actual.getData());
                });
    }

    @Test
    void deleteCustomer() {

    }

    @WithMockUser(username = "edy", roles = "ADMIN")
    @Test
    void shouldHave403StatusCodeWhenDeleteById() throws Exception {
        String id = "edy-1";
        when(userSecurity.checkCustomer(isA(Authentication.class), isA(String.class))).thenReturn(true);
        doNothing().when(customerService).deleteById(id);

        mockMvc.perform(delete("/api/v1/customers/" + id)
                        .contentType("application/json")
                ).andExpect(status().isForbidden())
                .andDo(result -> {
                    CommonResponse<Customer> actual = objectMapper
                            .readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    Assertions.assertEquals(403, actual.getStatusCode());
                });

    }
}