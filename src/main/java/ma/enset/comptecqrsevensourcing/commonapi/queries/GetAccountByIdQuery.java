package ma.enset.comptecqrsevensourcing.commonapi.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class GetAccountByIdQuery {
    private String id;
}
