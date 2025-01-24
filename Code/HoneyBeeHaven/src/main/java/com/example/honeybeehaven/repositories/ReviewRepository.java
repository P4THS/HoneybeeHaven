package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> findAllByItemid(Integer itemid);
}
