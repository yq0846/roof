package com.side.jiboong.common.component;

public interface MailSender {
    void send(String address, String subject, String content);
}
