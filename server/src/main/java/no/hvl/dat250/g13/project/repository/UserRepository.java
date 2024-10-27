package no.hvl.dat250.g13.project.repository;

import no.hvl.dat250.g13.project.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    public boolean existsByUsername(String username);
}
