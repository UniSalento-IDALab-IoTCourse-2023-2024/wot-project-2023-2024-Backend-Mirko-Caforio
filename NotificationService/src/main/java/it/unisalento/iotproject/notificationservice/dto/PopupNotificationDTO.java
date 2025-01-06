package it.unisalento.iotproject.notificationservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopupNotificationDTO extends NotificationDTO {
    private boolean read;
}
