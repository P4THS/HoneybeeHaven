package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.tables.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Integer> {

}
