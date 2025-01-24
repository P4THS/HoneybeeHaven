package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Business;
import com.example.honeybeehaven.tables.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BusinessRepository extends CrudRepository<Business, Integer> {
    Business findByEmail(String email);

    @Query("SELECT b FROM Business b WHERE b.email = :email AND b.password = :password")
    Business findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}