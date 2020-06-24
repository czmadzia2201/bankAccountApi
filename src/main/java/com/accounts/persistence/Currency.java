package com.accounts.persistence;

public enum Currency {
    PLN, USD;
    private Double value;

    public Double setValue(Double value) {
        if(this.equals(Currency.PLN))
            return 1.0;
        return value;
    }

}
