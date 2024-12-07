package com.project.system2.domain.entity;

import lombok.Data;

@Data
public class ActProcessDefinition {
    private String id;
    private String name;
    private String key;
    private Integer version;
    private String category;
    private String deploymentId;
    private String resourceName;
    private String diagramResourceName;
    private String description;
    private Boolean suspended;
    private Boolean hasStartFormKey;
    private Boolean hasGraphicalNotation;
    private String tenantId;
} 