package com.ivanfranchin.bitcoinclient.security;

import com.ivanfranchin.bitcoinclient.controller.UIController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UIController.class)
@Import(SecurityConfig.class)
class SecurityConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    void testUnauthenticatedRequestRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void testAuthenticatedRequestSucceeds() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void testCsrfIsDisabledForWebSocketEndpoint() throws Exception {
        // POST to /websocket/** without a CSRF token must not return 403 Forbidden.
        // Must be authenticated so Spring Security does not redirect to login first.
        mockMvc.perform(post("/websocket/info"))
                .andExpect(status().isNotFound()); // 404 (no handler), never 403
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void testLogoutRedirectsToLoginPage() throws Exception {
        mockMvc.perform(post("/logout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }
}
