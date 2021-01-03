package com.mycompany.asms.service;

import com.mycompany.asms.dao.ClientDAO;
import com.mycompany.asms.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ClientService implements EntityService {

    @Qualifier("client_dao")
    private final ClientDAO clientDAO;

    @Autowired
    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    @Override
    public long add(Entity entity) {
        return clientDAO.insert(entity);
    }

    @Override
    public Map<String, ? extends Entity> getByIDs(String[] ids) {
        return clientDAO.selectByIDs(ids);
    }

    @Override
    public void updateByID(String id, Entity newEntity) {
        clientDAO.updateByID(id, newEntity);
    }

    @Override
    public void deleteByIDs(String[] ids) {
        clientDAO.deleteByIDs(ids);
    }
}
