package ma.enset.comptecqrsevensourcing.commonapi.dtos;

import lombok.Data;

@Data
public class DebitAccountRequestDTO {
    private String accountId;
    private double amount;
    private String currency;
}
