package com.rms.jang_rms.modules.notification;

import com.rms.jang_rms.enums.NotificationStatus;
import com.rms.jang_rms.enums.NotificationType;
import com.rms.jang_rms.modules.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.UNREAD;

    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    private User recipient;
}
