package org.telegram.toto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.toto.repository.entities.Chat;

public interface TelegramRepo extends JpaRepository<Chat, String> {
}
