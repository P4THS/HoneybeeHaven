package com.example.honeybeehaven.services;

import com.example.honeybeehaven.repositories.PidRepository;
import com.example.honeybeehaven.tables.pid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PidService {
    @Autowired
    private PidRepository pidRepository;

    public Integer getNextId()
    {
        Iterable<pid> list = pidRepository.findAll();
        Integer next = 0;
        for (pid j: list) {
            next = j.getProductid();
        }

        pidRepository.deleteById(next);

        pid iid = new pid();
        iid.setProductid(next + 1);

        pidRepository.save(iid);

        return next;
    }
}