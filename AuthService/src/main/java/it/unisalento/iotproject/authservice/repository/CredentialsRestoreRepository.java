package it.unisalento.iotproject.authservice.repository;

import it.unisalento.iotproject.authservice.domain.CredentialsRestore;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CredentialsRestoreRepository extends MongoRepository<CredentialsRestore, String> {
    Optional<CredentialsRestore> findByToken(String token);
}
