package ma.enset.comptecqrsevensourcing.commands.aggregates;

import ma.enset.comptecqrsevensourcing.commonapi.commands.CreateAccountCommand;
import ma.enset.comptecqrsevensourcing.commonapi.commands.CreditAccountCommand;
import ma.enset.comptecqrsevensourcing.commonapi.commands.DebitAccountCommand;
import ma.enset.comptecqrsevensourcing.commonapi.enums.AccountStatus;
import ma.enset.comptecqrsevensourcing.commonapi.events.AccountActivatedEvent;
import ma.enset.comptecqrsevensourcing.commonapi.events.AccountCreatedEvent;
import ma.enset.comptecqrsevensourcing.commonapi.events.AccountCreditedEvent;
import ma.enset.comptecqrsevensourcing.commonapi.events.AccountDebitedEvent;
import ma.enset.comptecqrsevensourcing.commonapi.exceptions.AmountNegativeException;
import ma.enset.comptecqrsevensourcing.commonapi.exceptions.BalanceInsuffiscientException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
        //Required by Axon to build a default Aggregate prior to Event Sourcing
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        if(createAccountCommand.getInitialBalance() < 0) throw new RuntimeException("Initial balance cannot be negative");

        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency(),
                AccountStatus.CREATED));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getId();
        this.balance = event.getInitialBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        if(command.getAmount() < 0) throw new AmountNegativeException("Credit amount cannot be negative");
        //if(this.status != AccountStatus.ACTIVATED) throw new RuntimeException("Account is not activated");

        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        this.balance += event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        if(command.getAmount() < 0) throw new AmountNegativeException("Debit amount cannot be negative");
        if(this.balance < command.getAmount()) throw new BalanceInsuffiscientException("Insufficient balance ==>" + this.balance + " < " + command.getAmount());
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        this.balance -= event.getAmount();
    }

}
