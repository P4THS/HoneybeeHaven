package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Chemical;
import com.example.honeybeehaven.tables.Machinery;
import org.springframework.data.repository.CrudRepository;

import javax.crypto.Mac;
import java.util.List;

public interface MachineryRepository extends CrudRepository<Machinery, Integer> {
    List<Machinery> findAllByBusinessid(Integer businessId);

    List<Machinery> findAllByItemnameContaining(String search);
}
