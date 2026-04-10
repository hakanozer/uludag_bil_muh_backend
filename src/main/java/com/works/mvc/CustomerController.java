package com.works.mvc;

import com.works.dto.CustomerLoginRequestDto;
import com.works.dto.CustomerRegisterRequestDto;
import com.works.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

@Controller
@RequestMapping("mvc")
@RequiredArgsConstructor
public class CustomerController {

    final CustomerService customerService;

    @GetMapping("customer/login")
    public String login(Model model){
        model.addAttribute("customerLoginRequestDto", new CustomerLoginRequestDto());
        return "login";
    }

    @PostMapping("customer/login")
    public String login(
            @Valid CustomerLoginRequestDto customerLoginRequestDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }
        ResponseEntity customerResponseEntity = customerService.login(customerLoginRequestDto);
        if (customerResponseEntity.getStatusCode().is2xxSuccessful()) {
            return "redirect:/mvc/dashboard";
        }
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("customer/register")
    public String register(Model model){
        model.addAttribute("customerRegisterRequestDto", new CustomerRegisterRequestDto());
        return "register";
    }

    @PostMapping("customer/register")
    public String register(
            @Valid CustomerRegisterRequestDto customerRegisterRequestDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }
        ResponseEntity response = customerService.register(customerRegisterRequestDto);
        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:/mvc/customer/login";
        }
        model.addAttribute("registerError", true);
        return "register";

    }


}
