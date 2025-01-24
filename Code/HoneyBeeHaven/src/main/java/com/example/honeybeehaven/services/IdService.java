package com.example.honeybeehaven.services;
import com.example.honeybeehaven.tables.*;
import com.example.honeybeehaven.repositories.IdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdService {
    @Autowired
    private IdRepository idRepository;

    public Integer getNextId()
    {
        Iterable<id> list = idRepository.findAll();
        Integer next = 0;
        for (id j: list) {
            next = j.getUserid();
        }

        idRepository.deleteById(next);

        id iid = new id();
        iid.setUserid(next + 1);

        idRepository.save(iid);

        return next;
    }
}
