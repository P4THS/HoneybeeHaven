package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Chemical;
import com.example.honeybeehaven.tables.Service;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChemicalRepository extends CrudRepository<Chemical, Integer> {
    List<Chemical> findAllByBusinessid(Integer businessId);

    List<Chemical> findAllByItemnameContaining(String search);
}
