package com.accounts.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyCalculatorTest extends BaseTest {

    @BeforeEach
    public void init() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(getNBPResponse(mid));
    }

    @Test
    public void testCurrencyRate() {
        assertEquals(mid, currencyCalculator.getCurrencyRate());
    }

    @Test
    void testCalculate() {
        assertEquals(3000.0, currencyCalculator.calculate(Currency.PLN, Currency.PLN, 3000));
        assertEquals(9000.0, currencyCalculator.calculate(Currency.USD, Currency.PLN, 3000));
        assertEquals(1000.0, currencyCalculator.calculate(Currency.PLN, Currency.USD, 3000));
        assertEquals(3000.0, currencyCalculator.calculate(Currency.USD, Currency.USD, 3000));
    }

}