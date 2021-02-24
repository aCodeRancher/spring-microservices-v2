package com.in28minutes.microservices.currencyexchangeservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CurrencyExchangeRepositoryTest {

    @Autowired
    CurrencyExchangeRepository currencyExchangeRepository;

    public static Stream<Arguments> getCurrencies() {
        return Stream.of(Arguments.of("USD" , "INR", 65.00d),
                Arguments.of("EUR", "INR", 75.00d),
                Arguments.of("AUD", "INR", 25.00d));
    }

    @ParameterizedTest
    @MethodSource("getCurrencies")
    public void testRepository(String from, String to, double cm){
       CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
       assertTrue(currencyExchange.getConversionMultiple().doubleValue() ==cm);
    }

}
