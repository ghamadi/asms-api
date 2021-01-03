package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.HashSet;
import java.util.LinkedHashSet;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Client.class, name = "client"),
        @JsonSubTypes.Type(value = Payment.class, name = "payment"),
        @JsonSubTypes.Type(value = Permission.class, name = "permission"),
        @JsonSubTypes.Type(value = Product.class, name = "product"),
        @JsonSubTypes.Type(value = Project.class, name = "project"),
        @JsonSubTypes.Type(value = ProjectSection.class, name = "project_section"),
        @JsonSubTypes.Type(value = Role.class, name = "role"),
        @JsonSubTypes.Type(value = SectionCompartment.class, name = "section_compartment"),
        @JsonSubTypes.Type(value = User.class, name = "user")
})
public abstract class Entity {
    /**
     * Stores the fieldNames that are explicitly set by Jackson through the client's JSON file
     */
    protected LinkedHashSet<String> explicitlySetFields;

    protected Entity() {
        explicitlySetFields = new LinkedHashSet<>();
    }

    @JsonIgnore
    public LinkedHashSet<String> getExplicitlySetFields() {
        return explicitlySetFields;
    }

    /**
     * Updates the explicitlySetFields Set by adding the passed fieldName
     * @param fieldName to be marked as explicitly set
     */
    protected void markFieldAsSet(String fieldName) {
        explicitlySetFields.add(fieldName);
    }

    public abstract String getId();
}
