package com.works.mvc;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("mvc")
public class LoginController {

    String[] cities = {"Ankara", "Istanbul", "Izmir", "Bursa", "Adana"};

    @GetMapping("")
    public String login(Model model){
        model.addAttribute("title", "App Title");
        model.addAttribute("message", "App Message");
        model.addAttribute("cities", cities);
        return "login";
    }

    @PostMapping("login")
    public String login(@RequestParam String username){
        System.out.println(username);
        return "redirect:/mvc";
    }

    @PostMapping("register")
    public String register(RegisterUser registerUser){
        System.out.println(registerUser);
        return "redirect:/mvc";
    }


}

@Data
class RegisterUser {
    private String name;
    private String username;
    private String password;
}
