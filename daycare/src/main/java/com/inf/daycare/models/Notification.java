package com.inf.daycare.models;

import com.inf.daycare.enums.NotificationRecipientTypeEnum;
import com.inf.daycare.enums.NotificationTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "notification")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //Quién envía la notificación
    @JoinColumn(name = "sender_user_id", nullable = false)
    private User sender;

    private String title; // Título o asunto de la notificación
    private String content; // Cuerpo del mensaje de la notificación

    @Enumerated(EnumType.STRING) // Tipo de notificación: GLOBAL_DAYCARE, CLASSROOM, SPECIFIC_USER
    private NotificationTypeEnum type;

    @Enumerated(EnumType.STRING)
    private NotificationRecipientTypeEnum recipientType;

    // Relaciones opcionales según el 'reipientType'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daycare_id", nullable = true) // Para notificaciones GLOBAL_DAYCARE o CLASSROOM
    private Daycare targetDaycare; // A qué guardería aplica esta notificación

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = true) // Para notificaciones de tipo CLASSROOM
    private Classroom targetClassroom; // A qué aula aplica esta notificación

    // Si quieres enviar a un usuario específico (ej. un tutor):
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_id", nullable = true)
    private User targetUser; // Usuario específico al que va dirigida

    private LocalDateTime sentAt; // Fecha y hora de envío

    // Campo para que los usuarios puedan marcarla como leída (opcional, podrías tener una tabla intermedia si muchos usuarios leen)
    // private Boolean isRead = false; // Esta es solo para un escenario de "notificación a un solo usuario", si es global, necesitas una tabla de NotificacionLeida.

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
    }
}
