package com.in28minutes.microservices.currencyconversionservice;

import java.math.BigDecimal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@WebMvcTest
@AutoConfigureMockMvc
@ExtendWith(WireMockExtension.class)
class CurrencyConversionControllerTest {

    @MockBean
    CurrencyExchangeProxy currencyExchangeProxy;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WireMockServer wireMockServer;

    private String from = "USD";
    private String to = "INR";
    private long id = 20001l;
    private BigDecimal quantity = BigDecimal.valueOf(100);
    private BigDecimal multiple = BigDecimal.valueOf(65);
    private BigDecimal calAmount = BigDecimal.valueOf(6500);
    private String envn = "8082";
    private CurrencyConversionController currencyConversionController = new CurrencyConversionController();

     //configure a Wiremock at port 8000 to mock the currency-exchange
    @TestConfiguration
    static class RestTemplateBuilderProvider {
        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer(){
            WireMockServer server = with(wireMockConfig().port(8000));
            server.start();
            return server;
        }
    }

    @Test
    public void testConversion() throws JsonProcessingException {

        CurrencyConversion currencyConversion = new CurrencyConversion(id,from, to, quantity, multiple, calAmount, envn);
        //use WireMock server to mock the currency conversion output
        wireMockServer.stubFor(WireMock.get("/currency-exchange/from/USD/to/INR")
                .willReturn(okJson(objectMapper.writeValueAsString(currencyConversion))));
        CurrencyConversion currencyConversionOutput =
                currencyConversionController.calculateCurrencyConversion(from,to,quantity);
        assertTrue(currencyConversionOutput.getId()==id);
        assertTrue(currencyConversionOutput.getFrom().equals("USD"));
        assertTrue(currencyConversionOutput.getTo().equals("INR"));
        assertTrue(currencyConversionOutput.getConversionMultiple().doubleValue()==65.00);
        assertTrue(currencyConversionOutput.getQuantity().doubleValue()==100);
        assertTrue(currencyConversionOutput.getTotalCalculatedAmount().doubleValue()==6500.00);
        assertTrue(currencyConversionOutput.getEnvironment().equals("8082 rest template"));
    }

    @Test
    public void testConversionFeign() throws Exception {
        String uri = String.format("/currency-conversion-feign/from/%s/to/%s/quantity/%f",from, to, quantity);
        CurrencyConversion currencyConversion = new CurrencyConversion(id,from, to, quantity, multiple, calAmount, envn);
        when(currencyExchangeProxy.retrieveExchangeValue(from,to))
                .thenReturn(currencyConversion);
        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(jsonPath("$.id").value("20001"))
                .andExpect(jsonPath("$.from").value("USD"))
                .andExpect(jsonPath("$.to").value("INR"))
                .andExpect(jsonPath("$.quantity").value(100))
                .andExpect(jsonPath("$.conversionMultiple").value(65))
                .andExpect(jsonPath("$.totalCalculatedAmount").value(6500))
                .andExpect(jsonPath("$.environment").value(envn+ " "+ "feign"))
                .andExpect(status().isOk());
    }

}