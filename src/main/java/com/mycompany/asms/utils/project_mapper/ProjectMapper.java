package com.mycompany.asms.utils.project_mapper;

import com.mycompany.asms.model.Payment;
import com.mycompany.asms.model.Project;
import com.mycompany.asms.model.ProjectSection;
import com.mycompany.asms.model.SectionCompartment;

import java.util.LinkedHashMap;
import java.util.List;

public class ProjectMapper {

    private List<ComplexProject> complexProjects;

    public ProjectMapper(List<ComplexProject> complexProjects) {
        this.complexProjects = complexProjects;
    }

    public LinkedHashMap<String, Project> map() {
        LinkedHashMap<String, Project> map = new LinkedHashMap<String, Project>();
        for (ComplexProject cp : complexProjects) {
            Project project = cp.getProject();
            ProjectSection section = cp.getSection();
            SectionCompartment compartment = cp.getCompartment();
            Payment payment = cp.getPayment();

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

            Project storedProject = map.get(project.getId());

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
