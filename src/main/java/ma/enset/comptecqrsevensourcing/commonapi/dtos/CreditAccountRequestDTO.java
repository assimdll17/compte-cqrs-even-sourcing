package ma.enset.comptecqrsevensourcing.commonapi.dtos;

import lombok.Data;

@Data
public class CreditAccountRequestDTO {
    private String accountId;
    private double amount;
    private String currency;
}
