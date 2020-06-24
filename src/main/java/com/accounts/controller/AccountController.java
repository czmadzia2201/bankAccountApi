package com.accounts.controller;

import com.accounts.model.Account;
import com.accounts.model.RequestFailed;
import com.accounts.model.TransferData;
import com.accounts.persistence.InMemoryRepo;
import com.accounts.persistence.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    InMemoryRepo repository;

    @Autowired
    TransferService transferService;

    @GetMapping(value = "/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return new ResponseEntity(repository.getAccounts(), HttpStatus.OK);
    }

    @GetMapping(value = "/account/{pesel}")
    public ResponseEntity getAccountByPesel(@PathVariable(value = "pesel") String pesel) {
        Account account = repository.getAccountByPesel(pesel);
        if (account==null) {
            RequestFailed error = new RequestFailed(HttpStatus.NOT_FOUND, String.format("Account %s not found.", pesel));
            return ResponseEntity.status(error.getStatus()).body(error);
        }
        return new ResponseEntity(repository.getAccountByPesel(pesel), HttpStatus.OK);
    }

    @PostMapping(value = "/createaccount")
    public ResponseEntity createAccount(@Valid @RequestBody Account account, BindingResult result) {
        if(result.hasErrors()) {
            RequestFailed error = new RequestFailed(HttpStatus.BAD_REQUEST, getErrorMessagesFromBindingResult(result));
            return ResponseEntity.status(error.getStatus()).body(error);
        }
        if (repository.getAccountByPesel(account.getPesel())!=null) {
            RequestFailed error = new RequestFailed(HttpStatus.CONFLICT, String.format("Account %s already exists. One user can only have one account.", account.getPesel()));
            return ResponseEntity.status(error.getStatus()).body(error);
        }
        repository.createAccount(account);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping(value = "/transfermoney")
    public ResponseEntity transferMoney(@Valid @RequestBody TransferData transferData, BindingResult result) {
        if(result.hasErrors()) {
            RequestFailed error = new RequestFailed(HttpStatus.BAD_REQUEST, getErrorMessagesFromBindingResult(result));
            return ResponseEntity.status(error.getStatus()).body(error);
        }
        Account fromAccount = repository.getAccountByPesel(transferData.getFromPesel());
        Account toAccount = repository.getAccountByPesel(transferData.getToPesel());
        if(fromAccount==null || toAccount==null) {
            RequestFailed error = new RequestFailed(HttpStatus.NOT_FOUND, "Account not found.");
            return ResponseEntity.status(error.getStatus()).body(error);
        }
        boolean transferred = transferService.transferMoney(fromAccount, toAccount, transferData.getFromCurrency(), transferData.getToCurrency(), transferData.getAmount());
        if(!transferred) {
            RequestFailed error = new RequestFailed(HttpStatus.BAD_REQUEST, "Not enough money on the source account.");
            return ResponseEntity.status(error.getStatus()).body(error);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    private String getErrorMessagesFromBindingResult(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        result.getFieldErrors().forEach(f -> sb.append(f.getField() + ": " + f.getDefaultMessage() + "; "));
        return sb.toString();
    }
}
