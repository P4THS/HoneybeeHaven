package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Cart;
import com.example.honeybeehaven.tables.ServiceCart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ServiceCartRepository extends CrudRepository<ServiceCart, Integer> {
    @Query("SELECT c FROM Cart c WHERE c.userid = :userid AND c.itemid = :itemid")
    Optional<ServiceCart> findByUseridAndItemid(Integer userid, Integer itemid);

    List<ServiceCart> findAllByUserid(Integer userid);
    List<ServiceCart> findAllByItemid(Integer itemid);


    @Transactional
    @Modifying
    @Query("DELETE FROM ServiceCart e WHERE e.userid = :userid")
    void deleteAllByUserid(Integer userid);
}