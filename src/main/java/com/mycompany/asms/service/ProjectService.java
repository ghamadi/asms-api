package com.mycompany.asms.service;

import com.mycompany.asms.dao.ProjectDAO;
import com.mycompany.asms.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProjectService implements EntityService{

    @Qualifier("project_dao")
    private final ProjectDAO projectDAO;

    @Autowired
    public ProjectService(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public long add(Entity entity) {
        return projectDAO.insert(entity);
    }

    @Override
    public Map<String, ? extends Entity> getByIDs(String[] ids) {
        return projectDAO.selectByIDs(ids);
    }

    @Override
    public void updateByID(String id, Entity newEntity) {
        projectDAO.updateByID(id, newEntity);
    }

    @Override
    public void deleteByIDs(String[] ids) {
        projectDAO.deleteByIDs(ids);
    }
}
