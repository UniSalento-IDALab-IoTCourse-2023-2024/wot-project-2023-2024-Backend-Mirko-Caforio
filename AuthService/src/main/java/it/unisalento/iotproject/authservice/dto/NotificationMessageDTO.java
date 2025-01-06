package it.unisalento.iotproject.authservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessageDTO {
    private String receiver;
    private String message;
    private String subject;

    //type: ricevuta, notifica generica,
    // notifica di errore, notifica di avviso,
    // notifica di conferma, AUTH
    private String type;

    //Entrambi false mai, di default notifiche attive,
    // se si desidera solo email si mette a false uno ed email a true
    private boolean email;
    private boolean notification;
}
