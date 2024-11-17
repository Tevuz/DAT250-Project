package no.hvl.dat250.g13.project.repository;

import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.service.data.user.UserId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    boolean existsByUsername(String username);

    default boolean existsBy(UserId info) {
        if (info.id().isPresent())
            return existsById(info.id().get());
        if (info.username().isPresent())
            return existsByUsername(info.username().get());
        return false;
    }

    @Query("SELECT entity.id FROM UserEntity entity WHERE entity.username = :username")
    Optional<Long> findIdByUsername(String username);

    default Optional<Long> findIdBy(UserId info) {
        if (info.id().isPresent())
            return info.id();
        if (info.username().isPresent())
            return findIdByUsername(info.username().get());
        return Optional.empty();
    }

    Optional<UserEntity> findByUsername(String username);

    default Optional<UserEntity> findBy(UserId info) {
        System.out.println("findBy() : " + info);
        if (info.id().isPresent())
            return findById(info.id().get());
        if (info.username().isPresent())
            return findByUsername(info.username().get());
        return Optional.empty();
    }

    default void deleteBy(UserId info) {
        findIdBy(info).ifPresent(this::deleteById);
    }
}
