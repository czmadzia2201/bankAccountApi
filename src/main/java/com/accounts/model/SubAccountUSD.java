package com.accounts.model;

import com.accounts.persistence.Currency;

public class SubAccountUSD extends SubAccount {

    public SubAccountUSD(Double amount) {
        super(Currency.USD, amount);
    }

    public SubAccountUSD(Currency currency, Double amount) {
        super(Currency.USD, amount);
    }
}
