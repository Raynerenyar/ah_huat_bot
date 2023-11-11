package org.telegram.toto.repository.entities;

import jakarta.persistence.Column;

// import javax.persistence.Column;
// import javax.persistence.*;
// import javax.persistence.Table;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CHATS")
public class Chat {

    @Id
    @Column(name = "CHAT_ID")
    private String chatId;

    @Column(name = "ALERT_VALUE")
    private long alertValue;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public long getAlertValue() {
        return alertValue;
    }

    public void setAlertValue(long alertValue) {
        this.alertValue = alertValue;
    }

}