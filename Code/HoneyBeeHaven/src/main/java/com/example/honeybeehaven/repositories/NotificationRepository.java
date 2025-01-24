package com.example.honeybeehaven.repositories;

import com.example.honeybeehaven.classes.Notification;
import com.example.honeybeehaven.tables.Machinery;
import com.example.honeybeehaven.tables.Notifications;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notifications, Integer>  {

     List<Notifications> findAllByUserId(Integer userid);


}