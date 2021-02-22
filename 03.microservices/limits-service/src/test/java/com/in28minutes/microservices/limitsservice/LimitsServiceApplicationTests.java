package com.in28minutes.microservices.limitsservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class LimitsServiceApplicationTests {


	@Autowired
	MockMvc mockmvc;

	@Test
	void testLimits() throws Exception {
		mockmvc.perform(get("/limits"))
				.andExpect(jsonPath("$.minimum").value("3"))
				.andExpect(jsonPath("$.maximum").value("997"));
	}

}
