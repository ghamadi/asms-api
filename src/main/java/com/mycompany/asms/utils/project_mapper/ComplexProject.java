package com.mycompany.asms.utils.project_mapper;

import com.mycompany.asms.model.*;

public class ComplexProject extends Entity {

    private Project project;
    private ProjectSection section;
    private SectionCompartment compartment;
    private Payment payment;

    public ComplexProject(Project project, ProjectSection section, SectionCompartment compartment, Payment payment) {
        this.project = project;
        this.section = section;
        this.compartment = compartment;
        this.payment = payment;
    }

    public Project getProject() {
        return project;
    }

    public ProjectSection getSection() {
        return section;
    }

    public SectionCompartment getCompartment() {
        return compartment;
    }

    public Payment getPayment() {
        return payment;
    }

    @Override
    public String getId() {
        return project.getId();
    }
}
