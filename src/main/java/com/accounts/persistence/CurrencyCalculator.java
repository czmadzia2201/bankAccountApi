package com.accounts.persistence;

import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CurrencyCalculator {

    private RestTemplate restTemplate;

    private static String URL = "http://api.nbp.pl/api/exchangerates/rates/a/usd/";

    public CurrencyCalculator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CurrencyCalculator() {
        this(new RestTemplate());
    }

    public double calculate(Currency fromCurrency, Currency toCurrency, double amount) {
        Double currencyRate = getCurrencyRate();
        double ratio = fromCurrency.setValue(currencyRate)/toCurrency.setValue(currencyRate);
        return amount * ratio;
    }

    public double getCurrencyRate() {
        NBPResponse nbpResponse = restTemplate.getForObject(URL, NBPResponse.class);
        return nbpResponse.rates.get(0).mid;
    }

    static class NBPResponse {
        List<Rates> rates;
        public void setRates(List<Rates> rates) {
            this.rates = rates;
        }
    }

    static class Rates {
        Double mid;
        public void setMid(Double mid) {
            this.mid = mid;
        }
    }

}
