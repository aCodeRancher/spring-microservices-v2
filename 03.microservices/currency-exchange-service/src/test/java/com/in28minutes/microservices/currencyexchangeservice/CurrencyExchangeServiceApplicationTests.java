package com.in28minutes.microservices.currencyexchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyExchangeServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

    @ParameterizedTest
	@MethodSource("com.in28minutes.microservices.currencyexchangeservice.CurrenciesUtil#getCurrencies")
	void testCurrencyExchange(String from, String to, double cm) {
		String uri = String.format("http://localhost:%d/currency-exchange/from/%s/to/%s", port, from, to);
		CurrencyExchange currencyExchange = restTemplate.getForObject(uri, CurrencyExchange.class);
		assertTrue(currencyExchange.getConversionMultiple().doubleValue() == cm);
		assertTrue(currencyExchange.getEnvironment().equals(String.valueOf(port)));
	}

}
