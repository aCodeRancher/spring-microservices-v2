package com.in28minutes.microservices.limitsservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class LimitsServiceApplicationTests {

    @Autowired
    MockMvc mockmvc;

    @Test
    void testLimits() throws Exception {
        mockmvc.perform(get("/limits"))
                .andExpect(jsonPath("$.minimum").value("21"))
                .andExpect(jsonPath("$.maximum").value("2222"));
    }
}
