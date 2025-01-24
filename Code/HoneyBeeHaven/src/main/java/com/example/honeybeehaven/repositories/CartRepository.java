package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Cart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Integer> {
    @Query("SELECT c FROM Cart c WHERE c.userid = :userid AND c.itemid = :itemid")
    Optional<Cart> findByUseridAndItemid(Integer userid, Integer itemid);

    List<Cart> findAllByUserid(Integer userid);
    List<Cart> findAllByItemid(Integer itemid);

    @Transactional
    @Modifying
    @Query("DELETE FROM Cart e WHERE e.userid = :userid")
    void deleteAllByUserid(Integer userid);
}
