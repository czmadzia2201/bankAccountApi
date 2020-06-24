package com.accounts.persistence;

import com.accounts.model.Account;
import com.accounts.model.SubAccount;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    CurrencyCalculator currencyCalculator;

    public TransferService(CurrencyCalculator currencyCalculator) {
        this.currencyCalculator = currencyCalculator;
    }

    public TransferService() {
        this(new CurrencyCalculator());
    }

    public boolean transferMoney(Account fromAccount, Account toAccount, Currency fromCurrency, Currency toCurrency, double amount) {
        Boolean transferred = updateSourceSubAccount(fromAccount, fromCurrency, amount);
        if(transferred) {
            updateDestinationSubAccount(toAccount, fromCurrency, toCurrency, amount);
        }
        return transferred;
    }

    private boolean updateSourceSubAccount(Account account, Currency fromCurrency, double amount) {
        SubAccount source = account.getSubAccountByCurrency(fromCurrency);
        double currentAmount = source.getAmount();
        if(currentAmount >= amount) {
            source.setAmount(currentAmount - amount);
            return true;
        }
        return false;
    }

    private void updateDestinationSubAccount(Account account, Currency fromCurrency, Currency toCurrency, double amount) {
        SubAccount destination = account.getSubAccountByCurrency(toCurrency);
        double currentAmount = destination.getAmount();
        destination.setAmount(currentAmount + currencyCalculator.calculate(fromCurrency, toCurrency, amount));
    }
}
