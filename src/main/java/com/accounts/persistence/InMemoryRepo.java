package com.accounts.persistence;

import com.accounts.model.Account;
import com.accounts.model.SubAccountPLN;
import com.accounts.model.SubAccountUSD;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryRepo {

    private Map<String, Account> accounts = new HashMap();

    public InMemoryRepo() {
        init();
    }

    private void init() {
        accounts.put("85091500000", new Account("Jan", "Kowalski", "85091500000", new SubAccountUSD(0.0), new SubAccountPLN(5000.0)));
        accounts.put("79081500000", new Account("Krystian", "Nowak", "79081500000", new SubAccountUSD(300.0), new SubAccountPLN(7500.0)));
        accounts.put("92012700000", new Account("Agata", "Jabłońska", "92012700000", new SubAccountUSD(1000.0), new SubAccountPLN(1000.0)));
        accounts.put("75120100000", new Account("Monika", "Jarząbek", "75120100000", new SubAccountUSD(0.0), new SubAccountPLN(2000.0)));
    }

    public List<Account> getAccounts() {
        return new ArrayList(accounts.values());
    }

    public Account getAccountByPesel(String pesel) {
        return accounts.get(pesel);
    }

    public void createAccount(Account account) {
        accounts.put(account.getPesel(), account);
    }
}
