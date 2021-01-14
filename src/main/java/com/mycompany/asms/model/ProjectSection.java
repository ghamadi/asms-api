package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.LinkedHashMap;

@JsonPropertyOrder({"id", "project_id", "description", "compartments"})
public class ProjectSection extends Entity {

    @JsonProperty
    private String id;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty
    private String description;

    @JsonProperty
    private LinkedHashMap<String, SectionCompartment> compartments;

    public ProjectSection() {
        compartments = new LinkedHashMap<>();
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        markFieldAsSet("id");
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        markFieldAsSet("projectId");
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        markFieldAsSet("description");
        this.description = description;
    }

    public LinkedHashMap<String, SectionCompartment> getCompartments() {
        return compartments;
    }

    public void setCompartments(LinkedHashMap<String, SectionCompartment> compartments) {
        this.compartments = compartments;
    }

    public void addCompartment(SectionCompartment compartment){
        if(compartments == null)
            compartments = new LinkedHashMap<>();
        compartments.put(compartment.getId(), compartment);
    }

    public double totalDuePerSection(){
        if(compartments == null || compartments.isEmpty()) return 0;
        return compartments.values().stream()
                .map(c -> (c.getLength()*c.getWidth()*c.getUnitPrice()))
                .reduce(Double::sum)
                .get();
    }

    public double totalLengthsPerSection() {
        if(compartments == null || compartments.isEmpty()) return 0;
        return compartments.values().stream()
                .map(SectionCompartment::getLength)
                .reduce(Double::sum)
                .get();
    }

    public double totalWidthsPerSection() {
        if(compartments == null || compartments.isEmpty()) return 0;
        return compartments.values().stream()
                .map(SectionCompartment::getWidth)
                .reduce(Double::sum)
                .get();
    }
}
