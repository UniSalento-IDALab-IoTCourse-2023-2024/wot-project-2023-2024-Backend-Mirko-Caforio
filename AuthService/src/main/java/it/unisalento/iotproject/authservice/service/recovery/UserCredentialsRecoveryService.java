package it.unisalento.iotproject.authservice.service.recovery;

import it.unisalento.iotproject.authservice.business.recovery.RecoveryUtils;
import it.unisalento.iotproject.authservice.domain.CredentialsRestore;
import it.unisalento.iotproject.authservice.domain.User;
import it.unisalento.iotproject.authservice.dto.NotificationMessageDTO;
import it.unisalento.iotproject.authservice.exceptions.TokenException;
import it.unisalento.iotproject.authservice.exceptions.UserNotFoundException;
import it.unisalento.iotproject.authservice.repository.CredentialsRestoreRepository;
import it.unisalento.iotproject.authservice.repository.UserRepository;
import it.unisalento.iotproject.authservice.service.NotificationConstants;
import it.unisalento.iotproject.authservice.service.NotificationMessageHandler;
import it.unisalento.iotproject.authservice.service.UserCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static it.unisalento.iotproject.authservice.configuration.SecurityConfig.passwordEncoder;

@Service
public class UserCredentialsRecoveryService {

    private final UserRepository userRepository;
    private final UserCheckService userCheckService;
    private final CredentialsRestoreRepository credentialsRestoreRepository;
    private final NotificationMessageHandler notificationMessageHandler;

    @Autowired
    public UserCredentialsRecoveryService(UserRepository userRepository, CredentialsRestoreRepository credentialsRestoreRepository, NotificationMessageHandler notificationMessageHandler, UserCheckService userCheckService) {
        this.userRepository = userRepository;
        this.credentialsRestoreRepository = credentialsRestoreRepository;
        this.notificationMessageHandler = notificationMessageHandler;
        this.userCheckService = userCheckService;
    }

    public CredentialsRestore recoverCredentials(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        CredentialsRestore credentialsRestore = new CredentialsRestore();
        credentialsRestore.setEmail(email);
        credentialsRestore.setToken(RecoveryUtils.generateSafeToken());
        credentialsRestore.setRequestDate(LocalDateTime.now());
        credentialsRestore.setExpirationDate(LocalDateTime.now().plusMinutes(15));
        credentialsRestore.setUsed(false);
        credentialsRestoreRepository.save(credentialsRestore);

        try {
            notificationMessageHandler.sendNotificationMessage(
                    NotificationMessageDTO.builder()
                            .message(credentialsRestore.getToken())
                            .receiver(email)
                            .subject("Password recovery")
                            .type(NotificationConstants.AUTH_NOTIFICATION_TYPE)
                            .email(true)
                            .notification(false)
                            .build()
            );
        } catch (Exception e) {
            return credentialsRestore;
        }

        return credentialsRestore;
    }

    public void resetPassword(String token, String newPassword) {
        Optional<CredentialsRestore> ret = credentialsRestoreRepository.findByToken(token);

        if (ret.isEmpty()) {
            throw new TokenException("Request not found");
        }

        CredentialsRestore credentialsRestore = ret.get();

        if (credentialsRestore.getExpirationDate().isBefore(LocalDateTime.now()) || credentialsRestore.isUsed()) {
            throw new TokenException("Request expired");
        }

        User user = userRepository.findByEmail(credentialsRestore.getEmail());

        user.setPassword(passwordEncoder().encode(newPassword));
        userRepository.save(user);
        credentialsRestore.setUsed(true);
        credentialsRestoreRepository.save(credentialsRestore);
    }

    public List<CredentialsRestore> getAllCredentialsRestore() {
        return credentialsRestoreRepository.findAll();
    }

    public void changePassword(String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(userCheckService.getCurrentUserEmail());

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        if (!passwordEncoder().matches(oldPassword, user.getPassword())) {
            throw new TokenException("Old password is not correct");
        }
        user.setPassword(passwordEncoder().encode(newPassword));
        userRepository.save(user);
    }
}
