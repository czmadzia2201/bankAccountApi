package com.accounts.controller;

import com.accounts.model.Account;
import com.accounts.model.SubAccountPLN;
import com.accounts.model.SubAccountUSD;
import com.accounts.model.TransferData;
import com.accounts.persistence.Currency;
import com.accounts.persistence.InMemoryRepo;
import com.accounts.persistence.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InMemoryRepo repository;

    @MockBean
    private TransferService transferService;

    @BeforeEach
    protected void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        Mockito.when(repository.getAccounts()).thenReturn(new ArrayList(getAccounts().values()));
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                        "{\"name\":\"A\",\"lastName\":\"B\",\"pesel\":\"82012200000\",\"subaccountUSD\":{\"currency\":\"USD\",\"amount\":300.0},\"subaccountPLN\":{\"currency\":\"PLN\",\"amount\":1500.0}}," +
                        "{\"name\":\"C\",\"lastName\":\"D\",\"pesel\":\"34561200000\",\"subaccountUSD\":{\"currency\":\"USD\",\"amount\":400.0},\"subaccountPLN\":{\"currency\":\"PLN\",\"amount\":1200.0}}]" +
                        "\n"));
    }

    @Test
    public void testGetAccountByPesel_happyPath() throws Exception {
        initGetByPesel("82012200000");
        mockMvc.perform(MockMvcRequestBuilders.get("/account/82012200000"))
                .andExpect(status().isOk())
                .andExpect(content().json("" +
                        "{\"name\":\"A\",\"lastName\":\"B\",\"pesel\":\"82012200000\",\"subaccountUSD\":{\"currency\":\"USD\",\"amount\":300.0},\"subaccountPLN\":{\"currency\":\"PLN\",\"amount\":1500.0}}"));
    }

    @Test
    public void testGetAccountByPesel_notFound() throws Exception {
        initGetByPesel("82012300000");
        mockMvc.perform(MockMvcRequestBuilders.get("/account/82012300000"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\":\"NOT_FOUND\",\"message\":\"Account 82012300000 not found.\"}"));
    }

    @Test
    public void testCreateAccount_happyPath() throws Exception {
        Account account = getAccounts().get("82012200000");
        String accountJson = objectMapper.writeValueAsString(account);
        mockMvc.perform(MockMvcRequestBuilders.post("/createaccount").contentType(MediaType.APPLICATION_JSON_VALUE).content(accountJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateAccount_notValid() throws Exception {
        Account account = getAccounts().get("34561200000");
        String accountJson = objectMapper.writeValueAsString(account);
        mockMvc.perform(MockMvcRequestBuilders.post("/createaccount").contentType(MediaType.APPLICATION_JSON_VALUE).content(accountJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":\"BAD_REQUEST\",\"message\":\"pesel: person not adult by pesel; \"}"));
    }

    @Test
    public void testCreateAccount_accountConflict() throws Exception {
        Account account = getAccounts().get("82012200000");
        initGetByPesel("82012200000");
        String accountJson = objectMapper.writeValueAsString(account);
        mockMvc.perform(MockMvcRequestBuilders.post("/createaccount").contentType(MediaType.APPLICATION_JSON_VALUE).content(accountJson))
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"status\":\"CONFLICT\",\"message\":\"Account 82012200000 already exists. One user can only have one account.\"}"));
    }

    @Test
    public void testTransferMoney_happyPath() throws Exception {
        String transferDataJson = prepareTransferData("82012200000", "34561200000", true);
        mockMvc.perform(MockMvcRequestBuilders.put("/transfermoney").contentType(MediaType.APPLICATION_JSON_VALUE).content(transferDataJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testTransferMoney_badRequest() throws Exception {
        String transferDataJson = prepareTransferData(null, "34561200000", true);
        mockMvc.perform(MockMvcRequestBuilders.put("/transfermoney").contentType(MediaType.APPLICATION_JSON_VALUE).content(transferDataJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":\"BAD_REQUEST\",\"message\":\"fromPesel: must not be null; \"}"));
    }

    @Test
    public void testTransferMoney_accountNotFound() throws Exception {
        String transferDataJson = prepareTransferData("82012300000", "34561200000", true);
        mockMvc.perform(MockMvcRequestBuilders.put("/transfermoney").contentType(MediaType.APPLICATION_JSON_VALUE).content(transferDataJson))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\":\"NOT_FOUND\",\"message\":\"Account not found.\"}"));
    }

    @Test
    public void testTransferMoney_notEnoughMoney() throws Exception {
        String transferDataJson = prepareTransferData("82012200000", "34561200000", false);
        mockMvc.perform(MockMvcRequestBuilders.put("/transfermoney").contentType(MediaType.APPLICATION_JSON_VALUE).content(transferDataJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":\"BAD_REQUEST\",\"message\":\"Not enough money on the source account.\"}"));
    }

    private String prepareTransferData(String fromPesel, String toPesel, boolean transferred) throws Exception {
        double amount = 200.0;
        Mockito.when(transferService.transferMoney(Mockito.any(Account.class), Mockito.any(Account.class), eq(Currency.USD), eq(Currency.PLN), eq(amount))).thenReturn(transferred);
        initGetByPesel(fromPesel);
        initGetByPesel(toPesel);
        TransferData transferData = new TransferData(fromPesel, toPesel, Currency.USD, Currency.PLN, amount);
        return objectMapper.writeValueAsString(transferData);
    }

    private void initGetByPesel(String pesel) {
        Mockito.when(repository.getAccountByPesel(pesel)).thenReturn(getAccounts().get(pesel));
    }

    private Map<String, Account> getAccounts() {
        Map<String, Account> accounts = new HashMap();
        accounts.put("82012200000", new Account("A", "B", "82012200000", new SubAccountUSD(300.0), new SubAccountPLN(1500.0)));
        accounts.put("34561200000", new Account("C", "D", "34561200000", new SubAccountUSD(400.0), new SubAccountPLN(1200.0)));
        return accounts;
    }

}