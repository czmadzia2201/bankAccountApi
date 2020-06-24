package com.accounts.model;

import com.accounts.persistence.Currency;

import javax.validation.constraints.NotNull;

public class TransferData {
    @NotNull
    private String fromPesel;
    @NotNull
    private String toPesel;
    @NotNull
    private Currency fromCurrency;
    @NotNull
    private Currency toCurrency;
    @NotNull
    private Double amount;

    public String getFromPesel() {
        return fromPesel;
    }

    public void setFromPesel(String fromPesel) {
        this.fromPesel = fromPesel;
    }

    public String getToPesel() {
        return toPesel;
    }

    public void setToPesel(String toPesel) {
        this.toPesel = toPesel;
    }

    public Currency getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(Currency fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(Currency toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
