package com.accounts.model;

import com.accounts.persistence.Currency;

public class SubAccount {
    private Currency currency;
    private Double amount;

    public SubAccount(Currency currency, Double amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
