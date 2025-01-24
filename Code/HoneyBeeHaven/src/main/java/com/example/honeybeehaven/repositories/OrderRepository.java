package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Client;
import com.example.honeybeehaven.tables.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findAllByClientid(Integer clientid);
}
