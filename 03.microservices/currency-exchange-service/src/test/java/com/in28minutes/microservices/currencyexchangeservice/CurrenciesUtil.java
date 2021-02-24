package com.in28minutes.microservices.currencyexchangeservice;

import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

public class CurrenciesUtil {

    public static Stream<Arguments> getCurrencies() {
        return Stream.of(Arguments.of("USD" , "INR", 65.00d),
                Arguments.of("EUR", "INR", 75.00d),
                Arguments.of("AUD", "INR", 25.00d));
    }
}
