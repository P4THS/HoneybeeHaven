package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Business;
import com.example.honeybeehaven.tables.Conversation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends CrudRepository<Conversation, Integer> {
    List<Conversation> findAllByBusinessid(Integer businessid);
    List<Conversation> findAllByClientid(Integer clientid);
    @Query("SELECT c FROM Conversation c WHERE c.clientid = :clientid AND c.businessid = :businessid")
    Optional<Conversation> findByClientidAndBusinessid(@Param("clientid") Integer clientid, @Param("businessid") Integer businessid);
}
