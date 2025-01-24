package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
}
