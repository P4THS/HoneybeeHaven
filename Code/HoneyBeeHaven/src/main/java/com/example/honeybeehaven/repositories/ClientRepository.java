package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends CrudRepository<Client, Integer> {
    Client findByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.email = :email AND c.password = :password")
    Client findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
}