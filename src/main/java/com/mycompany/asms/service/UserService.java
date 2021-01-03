package com.mycompany.asms.service;

import com.mycompany.asms.dao.UserDAO;
import com.mycompany.asms.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class UserService implements EntityService {

    @Qualifier("user_dao")
    private final UserDAO userService;

    @Autowired
    public UserService(UserDAO userService) {
        this.userService = userService;
    }


    @Override
    public long add(Entity entity) {
        return userService.insert(entity);
    }

    @Override
    public Map<String, ? extends Entity> getByIDs(String[] ids) {
        return userService.selectByIDs(ids);
    }

    @Override
    public void updateByID(String id, Entity newEntity) {
        userService.updateByID(id, newEntity);
    }

    @Override
    public void deleteByIDs(String[] ids) {
        userService.deleteByIDs(ids);
    }
}
