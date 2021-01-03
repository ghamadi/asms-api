package com.mycompany.asms.service;

import com.mycompany.asms.model.Entity;

import java.util.Map;


public interface EntityService {

    long add(Entity entity);

    Map<String, ? extends Entity> getByIDs(String[] ids);

    void updateByID(String id, Entity newEntity);

    void deleteByIDs(String[] ids);

}
