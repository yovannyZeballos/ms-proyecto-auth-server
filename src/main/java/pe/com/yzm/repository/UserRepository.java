package pe.com.yzm.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.com.yzm.model.User;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
  Mono<User> findByEmail(String email);

  Mono<Boolean> existsByEmail(String email);
}
