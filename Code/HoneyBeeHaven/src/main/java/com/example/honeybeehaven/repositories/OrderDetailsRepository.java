package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.OrderDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Integer> {
    List<OrderDetails> findAllByOrderid(Integer orderid);


    OrderDetails findByOrderidAndItemid(Integer orderid, Integer itemid);
}
