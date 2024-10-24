package no.hvl.dat250.g13.project.repository;

import no.hvl.dat250.g13.project.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
