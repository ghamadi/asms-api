package com.mycompany.asms.service;

import com.mycompany.asms.model.Entity;

import java.util.Map;

public interface WeakEntityService {
    long add(Entity entity, String parentID);

    Map<String, ? extends Entity> getByIDs(String[] ids, String parentID);

    void updateByID(String id, Entity newEntity, String parentID);

    void deleteByIDs(String[] ids, String parentID);
}
