package ma.enset.comptecqrsevensourcing.query.repositories;

import ma.enset.comptecqrsevensourcing.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
