package com.mycompany.asms.model;


public class UserRole extends Entity {

    private String username, role;

    public UserRole(String username) {
        setUsername(username);
        setRole("ROLE_ADMIN");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        markFieldAsSet("username");
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        markFieldAsSet("role");
        this.role = role;
    }

    @Override
    public String getId() {
        return username+role;
    }
}
