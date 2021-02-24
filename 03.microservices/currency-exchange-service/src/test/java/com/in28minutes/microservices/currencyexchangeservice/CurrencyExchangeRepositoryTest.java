package com.in28minutes.microservices.currencyexchangeservice;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CurrencyExchangeRepositoryTest {

    @Autowired
    CurrencyExchangeRepository currencyExchangeRepository;


    @ParameterizedTest
    @MethodSource("com.in28minutes.microservices.currencyexchangeservice.CurrenciesUtil#getCurrencies")
    public void testRepository(String from, String to, double cm){
       CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
       assertTrue(currencyExchange.getConversionMultiple().doubleValue() ==cm);
    }

}
