package com.works.controller;

import com.works.dto.CustomerLoginRequestDto;
import com.works.dto.CustomerRegisterRequestDto;
import com.works.entity.Customer;
import com.works.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("customer")
public class CustomerRestController {

    final CustomerService customerService;

    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody CustomerRegisterRequestDto customerRegisterRequestDto){
        return customerService.register(customerRegisterRequestDto);
    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody CustomerLoginRequestDto customerLoginRequestDto){
        return customerService.login(customerLoginRequestDto);
    }

    @GetMapping("logout")
    public void logout(){
        customerService.logout();
    }

}
