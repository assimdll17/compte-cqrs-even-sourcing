package ma.enset.comptecqrsevensourcing.query.repositories;

import ma.enset.comptecqrsevensourcing.query.entities.Account;
import ma.enset.comptecqrsevensourcing.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
