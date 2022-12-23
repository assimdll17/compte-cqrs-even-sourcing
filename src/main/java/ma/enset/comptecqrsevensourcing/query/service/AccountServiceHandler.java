package ma.enset.comptecqrsevensourcing.query.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.comptecqrsevensourcing.commonapi.enums.OperationType;
import ma.enset.comptecqrsevensourcing.commonapi.events.AccountActivatedEvent;
import ma.enset.comptecqrsevensourcing.commonapi.events.AccountCreatedEvent;
import ma.enset.comptecqrsevensourcing.commonapi.events.AccountCreditedEvent;
import ma.enset.comptecqrsevensourcing.commonapi.events.AccountDebitedEvent;
import ma.enset.comptecqrsevensourcing.commonapi.queries.GetAccountByIdQuery;
import ma.enset.comptecqrsevensourcing.commonapi.queries.GetAllAccountsQuery;
import ma.enset.comptecqrsevensourcing.query.entities.Account;
import ma.enset.comptecqrsevensourcing.query.entities.Operation;
import ma.enset.comptecqrsevensourcing.query.repositories.AccountRepository;
import ma.enset.comptecqrsevensourcing.query.repositories.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountServiceHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("*******************************");
        log.info("AccountCreatedEvent received");
        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getInitialBalance());
        account.setCurrency(event.getCurrency());
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountActivatedEvent event) {
        log.info("*******************************");
        log.info("AccountActivatedEvent received");
        Account account = accountRepository.getById(event.getId());
        account.setStatus(event.getStatus());
        accountRepository.save(account); //On fait save ou on met l'annotation @Transactional sur la classe qui fait la meme chose
    }

    @EventHandler
    public void on(AccountDebitedEvent event) {
        log.info("*******************************");
        log.info("AccountDebitedEvent received");
        Account account = accountRepository.getById(event.getId());
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());
        operation.setType(OperationType.DEBIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance() - event.getAmount());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent event) {
        log.info("*******************************");
        log.info("AccountCreditedEvent received");
        Account account = accountRepository.getById(event.getId());
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());
        operation.setType(OperationType.CREDIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance() + event.getAmount());
        accountRepository.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query)   {
        return accountRepository.findAll();
    }

    @QueryHandler
    public Account on(GetAccountByIdQuery query) {
        return accountRepository.findById(query.getId()).get();
    }
}
