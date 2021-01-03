package com.mycompany.asms.utils.project_mapper;

import com.mycompany.asms.model.Project;

import java.util.LinkedHashMap;
import java.util.List;

public class ProjectMapper {

    private List<ComplexProject> complexProjects;

    public ProjectMapper(List<ComplexProject> complexProjects) {
        this.complexProjects = complexProjects;
    }

    public LinkedHashMap<String, Project> map() {
        var map = new LinkedHashMap<String, Project>();
        for (var cp : complexProjects) {
            var project = cp.getProject();
            var section = cp.getSection();
            var compartment = cp.getCompartment();
            var payment = cp.getPayment();

            if(project == null)
                continue;

            if(payment != null)
                project.addPayment(payment);

            if(compartment != null)
                section.addCompartment(compartment);

            if(section != null)
                project.addSection(section);

            if (!map.containsKey(project.getId())) {
                map.put(project.getId(), project);
                continue;
            }

            var storedProject = map.get(project.getId());

            if(payment != null && !storedProject.hasPayment(payment))
                storedProject.addPayment(payment);

            if (section != null && !storedProject.containsSection(section)) {
                storedProject.addSection(section);
                continue;
            }

            if(compartment != null)
                storedProject.getSections().get(section.getId()).addCompartment(compartment);
        }
        return map;
    }
}
