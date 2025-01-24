package com.example.honeybeehaven.repositories;
import com.example.honeybeehaven.tables.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IdRepository extends CrudRepository<id, Integer> {

}
