package com.rms.jang_rms.modules.notification;

import com.rms.jang_rms.modules.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByTimestampDesc(User user);
}
