package com.ivanfranchin.bitcoinclient.controller;

import com.ivanfranchin.bitcoinclient.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UIController.class)
@Import(SecurityConfig.class)
class UIControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    @WithMockUser
    void testGetPricesReturnsViewNamePrices() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("prices"));
    }

    @Test
    void testGetPricesRedirectsToLoginWhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }
}
