package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {

    Optional<Subscription> findByBusinessid(Integer businessid);

    Optional<Subscription> findByBusinessidAndExpired(Integer businessid, Boolean expired);

    List<Subscription> findAllByExpired(Boolean expired);
}