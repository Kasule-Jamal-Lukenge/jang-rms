package com.rms.jang_rms.modules.notification;

import com.rms.jang_rms.enums.NotificationType;
import com.rms.jang_rms.modules.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender javaMailSender;

    public void notify(User recipient, String title, String message, NotificationType type) {

        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .type(type)
                .build();
        notificationRepository.save(notification);

        // ------------------------------------------  EMAIL NOTIFICATION  -------------------------------------------------
        try{
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(recipient.getEmail());
            email.setSubject(title);
            email.setText(message);
            javaMailSender.send(email);
        }catch(Exception e){
            System.out.println("Email Sending Failed: " + e.getMessage());
        }
    }
}
