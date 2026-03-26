package com.works.controller;

import com.works.dto.CustomerRegisterRequestDto;
import com.works.entity.Customer;
import com.works.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerRestController {

    final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody CustomerRegisterRequestDto customerRegisterRequestDto){
        return customerService.register(customerRegisterRequestDto);
    }

}
