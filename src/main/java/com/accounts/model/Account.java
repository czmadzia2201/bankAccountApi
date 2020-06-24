package com.accounts.model;

import com.accounts.persistence.Currency;
import com.accounts.validator.AdultByPesel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Account {
    @NotNull
    private String name;
    @NotNull
    private String lastName;
    @NotNull
    @Pattern(regexp="[\\d]{11}", message="may not contain non-numeric characters, size must be 11")
    @AdultByPesel
    private String pesel;
    @NotNull
    private SubAccountUSD subaccountUSD;
    @NotNull
    private SubAccountPLN subaccountPLN;

    public Account(String name, String lastName, String pesel, SubAccountUSD subaccountUSD, SubAccountPLN subaccountPLN) {
        this.name = name;
        this.lastName = lastName;
        this.pesel = pesel;
        this.subaccountUSD = subaccountUSD;
        this.subaccountPLN = subaccountPLN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public SubAccountUSD getSubaccountUSD() {
        return subaccountUSD;
    }

    public void setSubaccountUSD(SubAccountUSD subaccountUSD) {
        this.subaccountUSD = subaccountUSD;
    }

    public SubAccountPLN getSubaccountPLN() {
        return subaccountPLN;
    }

    public void setSubaccountPLN(SubAccountPLN subaccountPLN) {
        this.subaccountPLN = subaccountPLN;
    }

    public SubAccount getSubAccountByCurrency(Currency currency) {
        if(currency.equals(subaccountPLN.getCurrency()))
            return subaccountPLN;
        return subaccountUSD;
    }

}
