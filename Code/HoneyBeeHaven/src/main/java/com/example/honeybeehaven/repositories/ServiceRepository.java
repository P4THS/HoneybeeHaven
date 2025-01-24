package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Machinery;
import com.example.honeybeehaven.tables.Service;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServiceRepository extends CrudRepository<Service, Integer> {
 List<Service> findAllByBusinessid(Integer businessId);
 List<Service> findAllByItemnameContaining(String search);

}