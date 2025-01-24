package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.ReportedClients;
import org.springframework.data.repository.CrudRepository;

public interface ReportedClientRepository extends CrudRepository<ReportedClients, Integer> {

}
