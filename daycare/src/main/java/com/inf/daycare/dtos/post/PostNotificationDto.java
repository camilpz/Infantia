package com.inf.daycare.dtos.post;

import com.inf.daycare.enums.NotificationRecipientTypeEnum;
import com.inf.daycare.enums.NotificationTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostNotificationDto {
    @NotBlank(message = "El título de la notificación no puede estar vacío")
    private String title;

    @NotBlank(message = "El contenido de la notificación no puede estar vacío")
    private String content;

    @NotNull(message = "El tipo de notificación es requerido")
    private NotificationTypeEnum type;

    @NotNull(message = "El tipo de destinatario es requerido")
    private NotificationRecipientTypeEnum recipientType;

    // Estos campos son opcionales y dependen del 'type'
    private Long targetDaycareId; // Solo si type es GLOBAL_DAYCARE o CLASSROOM
    private Long targetClassroomId;
}
