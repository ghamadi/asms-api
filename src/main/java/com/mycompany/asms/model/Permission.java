package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;

public class Permission extends Entity {

    @JsonProperty
    private String id, permission;

    @JsonProperty("roles_permitted")
    private LinkedHashMap<String, Role> rolesPermitted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        markFieldAsSet("id");
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        markFieldAsSet("permission");
        this.permission = permission;
    }

    public LinkedHashMap<String, Role> getRolesPermitted() {
        return rolesPermitted;
    }

    public void setRolesPermitted(LinkedHashMap<String, Role> rolesPermitted) {
        this.rolesPermitted = rolesPermitted;
    }
}
