package com.works.controller;

import lombok.RequiredArgsConstructor;
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

    // Note ödev - Entity
    // id(Long), title(String), detail(String), day(Integer)
    // Save
    // SaveAll
    // Delete
    // Update
    // List - Sayafalandırma (page)
    // Search (title, detail) (page)
}


