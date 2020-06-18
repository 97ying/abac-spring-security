package com.example.permission.authority.model;

public enum TargetEnum {
    ROLE("role"),
    ORG_ID("orgId"),
    ORG_FULL_PATH("orgFullPath");

    private String name;

    TargetEnum(String name) {
        this.name = name;
    }
}
