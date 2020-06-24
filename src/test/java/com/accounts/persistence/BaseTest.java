package com.accounts.persistence;

import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class BaseTest {

    protected RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    protected CurrencyCalculator currencyCalculator = new CurrencyCalculator(restTemplate);

    protected double mid = 3.0;

    protected CurrencyCalculator.NBPResponse getNBPResponse(double mid) {
        CurrencyCalculator.Rates rates = new CurrencyCalculator.Rates();
        rates.setMid(mid);
        CurrencyCalculator.NBPResponse nbpResponse = new CurrencyCalculator.NBPResponse();
        nbpResponse.setRates(Arrays.asList(rates));
        return nbpResponse;
    }

}
