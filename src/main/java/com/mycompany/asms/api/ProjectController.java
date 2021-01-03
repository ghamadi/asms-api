package com.mycompany.asms.api;

import com.mycompany.asms.model.Entity;
import com.mycompany.asms.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/projects")
public class ProjectController implements EntityController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public long add(@Valid Entity entity) {
        return projectService.add(entity);
    }

    @Override
    public Map<String, ? extends Entity> getByIDs(String[] ids) {
        return projectService.getByIDs(ids);
    }

    @Override
    public void updateByID(@Valid String id, @Valid Entity entity) {
        projectService.updateByID(id, entity);
    }

    @Override
    public void deleteByIDs(String[] ids) {
        projectService.deleteByIDs(ids);
    }
}
