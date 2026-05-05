package com.works.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("note")
@RequiredArgsConstructor
public class NoteRestController {

    @GetMapping("list")
    public String list(){
        return "Note List";
    }

}


