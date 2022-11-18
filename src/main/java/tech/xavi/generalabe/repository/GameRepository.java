package tech.xavi.generalabe.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tech.xavi.generalabe.document.Game;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
}
