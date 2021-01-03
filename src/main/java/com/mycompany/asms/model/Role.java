package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;

import java.util.LinkedHashMap;

public class Role extends Entity implements GrantedAuthority {

    @JsonProperty
    private String id, title;

    @JsonProperty("permissions_given")
    private LinkedHashMap<String, Permission> permissionsGiven;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        markFieldAsSet("id");
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        markFieldAsSet("title");
        this.title = title;
    }

    public LinkedHashMap<String, Permission> getPermissionsGiven() {
        return permissionsGiven;
    }

    public void setPermissionsGiven(LinkedHashMap<String, Permission> permissionsGiven) {
        this.permissionsGiven = permissionsGiven;
    }

    @Override
    public String getAuthority() {
        return title;
    }
}
