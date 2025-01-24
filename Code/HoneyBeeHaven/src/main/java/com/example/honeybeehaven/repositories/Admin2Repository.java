package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Admin2;
import com.example.honeybeehaven.tables.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface Admin2Repository extends CrudRepository<Admin2, Integer> {

    Admin2 findByAdminEmail(String email);

    Admin2 findByAdminEmailAndAdminPassword(String email, String password);



}
