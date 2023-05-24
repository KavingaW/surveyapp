package com.hsenid.surveyapp.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Role {

    private String id;
    private RoleEnum name;

    public Role() {
    }

    public Role(String id, RoleEnum name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }
}
