package com.works.controller;

import com.works.CacheTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(NoteRestController.class)
@Import(CacheTestConfig.class)
public class NoteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test GET /note/list - Valid Response")
    void testList() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/note/list")
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("Note List"));
    }
}