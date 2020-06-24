package com.accounts.model;

import com.accounts.persistence.Currency;

public class SubAccountPLN extends SubAccount {

    public SubAccountPLN(Double amount) {
        super(Currency.PLN, amount);
    }

    public SubAccountPLN(Currency currency, Double amount) {
        super(Currency.PLN, amount);
    }

}
