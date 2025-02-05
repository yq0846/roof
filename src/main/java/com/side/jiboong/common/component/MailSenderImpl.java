package com.side.jiboong.common.component;

import com.side.jiboong.common.exception.MailSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    @Async
    public void send(String address, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.addRecipients(MimeMessage.RecipientType.TO, address);
            message.setFrom(mailProperties.getUsername());
            message.setSubject(subject);
            message.setText(content, "UTF-8", "html");
        } catch (MessagingException e) {
            throw new MailSendException(e.getMessage());
        }

        javaMailSender.send(message);
    }
}
