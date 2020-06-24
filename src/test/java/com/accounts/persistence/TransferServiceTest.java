package com.accounts.persistence;

import com.accounts.model.Account;
import com.accounts.model.SubAccountPLN;
import com.accounts.model.SubAccountUSD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransferServiceTest extends BaseTest {

    private TransferService transferService = new TransferService(currencyCalculator);

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    public void init() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(getNBPResponse(mid));
        fromAccount = new Account("A", "B", "12345000000", new SubAccountUSD(300.0), new SubAccountPLN(1500.0));
        toAccount = new Account("C", "D", "23451000000", new SubAccountUSD(600.0), new SubAccountPLN(800.0));
    }

    @Test
    public void testTransferPLNtoUSD() {
        transferService.transferMoney(fromAccount, toAccount, Currency.PLN, Currency.USD, 300.0);
        assertExpectedValues(300.0, 1200.0, 700.0, 800.0, fromAccount, toAccount);
    }

    @Test
    public void testTransferUSDtoPLN() {
        transferService.transferMoney(fromAccount, toAccount, Currency.USD, Currency.PLN, 100.0);
        assertExpectedValues(200.0, 1500.0, 600.0, 1100.0, fromAccount, toAccount);
    }

    @Test
    public void testTransferUSDtoUSD() {
        transferService.transferMoney(fromAccount, toAccount, Currency.USD, Currency.USD, 100.0);
        assertExpectedValues(200.0, 1500.0, 700.0, 800.0, fromAccount, toAccount);
    }

    @Test
    public void testTransferPLNtoPLN() {
        transferService.transferMoney(fromAccount, toAccount, Currency.PLN, Currency.PLN, 300.0);
        assertExpectedValues(300.0, 1200.0, 600.0, 1100.0, fromAccount, toAccount);
    }

    @Test
    public void testTransferCancelled() {
        transferService.transferMoney(fromAccount, toAccount, Currency.USD, Currency.PLN, 400.0);
        assertExpectedValues(300.0, 1500.0, 600.0, 800.0, fromAccount, toAccount);
    }

    private void assertExpectedValues(double fromUSD, double fromPLN, double toUSD, double toPLN, Account fromAccount, Account toAccount) {
        assertEquals(fromUSD, fromAccount.getSubaccountUSD().getAmount());
        assertEquals(fromPLN, fromAccount.getSubaccountPLN().getAmount());
        assertEquals(toUSD, toAccount.getSubaccountUSD().getAmount());
        assertEquals(toPLN, toAccount.getSubaccountPLN().getAmount());
    }

}