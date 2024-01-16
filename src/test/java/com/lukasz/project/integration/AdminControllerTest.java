package com.lukasz.project.integration;

import com.lukasz.project.database.auth.AuthenticationService;
import com.lukasz.project.database.auth.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class AdminControllerTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindRecruiters() throws Exception {

        RegisterRequest recruiter = RegisterRequest.builder()
                .name("John")
                .surname("Doe")
                .pesel("12345678901")
                .country("USA")
                .email("john.doe@example.com")
                .username("johndoe")
                .password("password123")
                .build();

        authenticationService.registerRecruiter(recruiter);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/findRecruiters")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        String content = result.getResponse().getContentAsString();
        String expectedContent = "[{\"id\":1,\"name\":\"John\",\"surname\":\"Doe\",\"pesel\":\"12345678901\"," +
                "\"country\":\"USA\",\"username\":\"john.doe@example.com\",\"email\":\"john.doe@example.com\"," +
                "\"role\":\"RECRUITER\",\"company\":null,\"createdOffers\":[],\"enabled\":true}]";

        assertEquals(expectedContent, content);
    }

    @Test
    @DirtiesContext
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindAdmins() throws Exception {


        // Dodaj admina
        RegisterRequest admin = RegisterRequest.builder()
                .name("John")
                .surname("Doe")
                .pesel("12345678901")
                .country("USA")
                .email("john.doe@example.com")
                .username("johndoe")
                .password("password123")
                .build();

        authenticationService.registerAdmin(admin);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/findAdmins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Sprawdź odpowiedź
        String content = result.getResponse().getContentAsString();
        assertEquals("[{\"id\":1,\"name\":\"John\",\"surname\":\"Doe\",\"pesel\":\"12345678901\"," +
                "\"country\":\"USA\",\"username\":\"john.doe@example.com\"," +
                "\"email\":\"john.doe@example.com\",\"role\":\"ADMIN\",\"enabled\":true}]", content);
    }

}

