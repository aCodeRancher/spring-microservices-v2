package com.in28minutes.microservices.springcloudconfigserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest
@AutoConfigureMockMvc
class SpringCloudConfigServerApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	void testQA() throws Exception {
		//The default profile values are included
		mockMvc.perform(get("/limits-service/qa"))
				.andExpect(jsonPath("$.profiles.[0]").value("qa"))
		        .andExpect(jsonPath("$.propertySources.[0].source.['limits-service.minimum']").value("21"))
				.andExpect(jsonPath("$.propertySources.[0].source.['limits-service.maximum']").value("2222"))
				.andExpect(jsonPath("$.propertySources.[1].source.['limits-service.minimum']").value("1"))
				.andExpect(jsonPath("$.propertySources.[1].source.['limits-service.maximum']").value("1111"));
    }

	@Test
	void testDev() throws Exception {
		//The default profile values are included
		mockMvc.perform(get("/limits-service/dev"))
				.andExpect(jsonPath("$.profiles.[0]").value("dev"))
				.andExpect(jsonPath("$.propertySources.[0].source.['limits-service.minimum']").value("1222"))
				.andExpect(jsonPath("$.propertySources.[0].source.['limits-service.maximum']").value("9999"))
				.andExpect(jsonPath("$.propertySources.[1].source.['limits-service.minimum']").value("1"))
				.andExpect(jsonPath("$.propertySources.[1].source.['limits-service.maximum']").value("1111"));
	}

	@Test
	void testMicroX() throws Exception {
		//The default profile values are included
		mockMvc.perform(get("/microservice-x/dev"))
				.andExpect(jsonPath("$.profiles.[0]").value("dev"))
				.andExpect(jsonPath("$.propertySources.[0].source.profile").value("dev"))
				.andExpect(jsonPath("$.propertySources.[0].source.['microservice-x.name']").value("x"))
				.andExpect(jsonPath("$.propertySources.[0].source.['microservice-x.developer']").value("helen"))
				.andExpect(jsonPath("$.propertySources.[0].source.['microservice-x.description']").value("Fun Service"));
	}
}
