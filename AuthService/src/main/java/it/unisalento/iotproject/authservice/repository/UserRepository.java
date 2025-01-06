package it.unisalento.iotproject.authservice.repository;


import it.unisalento.iotproject.authservice.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String>{
    List<User> findBySurname(String surname);
    List<User> findByNameAndSurname(String name, String surname);
    User findByEmail(String email);
}
