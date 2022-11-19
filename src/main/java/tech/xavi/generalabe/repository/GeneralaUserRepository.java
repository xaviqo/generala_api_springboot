package tech.xavi.generalabe.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.xavi.generalabe.document.GeneralaUser;

import java.util.Optional;

@Repository
public interface GeneralaUserRepository extends MongoRepository<GeneralaUser, String> {
    Optional<GeneralaUser> findUserByNickname(String nickname);
}