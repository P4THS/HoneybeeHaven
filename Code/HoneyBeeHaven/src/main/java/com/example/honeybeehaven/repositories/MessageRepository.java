package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    List<Message> findAllByConversationid(Integer convid);
}
