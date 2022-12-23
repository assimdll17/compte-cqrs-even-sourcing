package ma.enset.comptecqrsevensourcing.commonapi.commands;

import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public abstract class BaseCommand <T> {
    @TargetAggregateIdentifier
    @Getter T id;

    protected BaseCommand(T id) {
        this.id = id;
    }
}
